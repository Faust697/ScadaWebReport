package com.example.ScadaWebReport.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.ScadaWebReport.Model.TagInfo;
import com.example.ScadaWebReport.Model.Taglog.Taglog;
import com.example.ScadaWebReport.Model.staticInfo.StaticInfoModel;
import com.example.ScadaWebReport.repos.StaticInfoRepo;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;

@Service
public class dataProcessingService {

	private TaglogRepositoryImpl taglogRepositoryImpl;
	private final StaticInfoRepo staticInfoRepo;

	@Autowired
	public dataProcessingService(TaglogRepo taglogRepo, TaglogRepositoryImpl taglogRepositoryImpl,
			StaticInfoRepo staticInfoRepo) {
		this.taglogRepositoryImpl = taglogRepositoryImpl;

		this.staticInfoRepo = staticInfoRepo;
	}

	//Вызов актуальной таблицы 
	public String actualTable()
	{
		return "log."+"\"tag_log_" + formatCurrentMonth() + "\"";
	}
	//Получение данных из базы
	public List<Taglog> getLatestLogsForTags(String typeOfSearch, String additionalFilter) {

		List<String> filteredTagIds = null;
		List<StaticInfoModel> filteredTags = staticInfoRepo.findAll();

		//Смотрим, какие данные мы ищем
		switch (typeOfSearch) {
		case "level":
			filteredTagIds = filteredTags.stream().map(StaticInfoModel::getLevelId).collect(Collectors.toList());
			break;
		case "total":
			filteredTagIds = filteredTags.stream().map(StaticInfoModel::getTotalId).collect(Collectors.toList());
			break;
		default:
			filteredTagIds = filteredTags.stream().map(StaticInfoModel::getOnlineId).collect(Collectors.toList());
		}

		// Преобразуйте список ID тегов в строку с разделителями
		String tagIdString1 = String.join(",", filteredTagIds);

		// Получите последние записи логов для каждого тега(за последний месяц)
		List<Taglog> latestLogs = taglogRepositoryImpl
				.findLatestLogForEachTag("\"tag_log_" + formatCurrentMonth() + "\"", tagIdString1, additionalFilter);

		// Если в latestLogs меньше строк данных, чем в staticInfoRepo.findAll();,
		// просто заполняем пустыми полям
		if (latestLogs.size() < staticInfoRepo.count()) {

			List<String> missingTagIds = filteredTagIds.stream().filter(
					tagId -> !latestLogs.stream().anyMatch(taglog -> taglog.getTag_id().toString().equals(tagId)))
					.collect(Collectors.toList());

			for (String id : missingTagIds) {
				Taglog emptyLog = new Taglog();
				emptyLog.setTag_id(new BigInteger(id));
				latestLogs.add(emptyLog);

			}

		}

		/*
		 * Тут можно включить поиск по всей базе, если в ближайшем месяце не нашло if
		 * (!missingTagIds.isEmpty()) { String missingTagIdString = String.join(",",
		 * missingTagIds); List<Taglog> additionalLogs =
		 * taglogRepositoryImpl.findLatestLogForEachTag( "\"tag_log\"",
		 * missingTagIdString, additionalFilter);
		 * 
		 * 
		 * 
		 * latestLogs.addAll(additionalLogs); }
		 * 
		 * }
		 */
		return latestLogs;
	}

	// Работа с джейсонами, составление списков и прочее

	public List<TagLogWithName> getTagLogsWithNames(String requestType, String additionalFilter, boolean Level) {

		List<StaticInfoModel> filteredTags = staticInfoRepo.findAll();// get date from DB
		Taglog LevelObj = null;
		String LevelValue = null;
		String RequestColumn = null;

		List<Taglog> latestLogs = getLatestLogsForTags(requestType, additionalFilter);
		List<Taglog> latestLogsLevel = getLatestLogsForTags("level", additionalFilter);

		
		//Строим список для отправления в модель
		List<TagLogWithName> tagLogsWithNames = new ArrayList<>();
		for (StaticInfoModel filteredTag : filteredTags) {
			//Если нулл то ничего не делаем и идём к следующему
			if (filteredTag.getOnlineId() == null)
				continue;
			
			//Задаём параметры, которые уже под рукой
			String tagName = filteredTag.getName();
			String tagText = filteredTag.getPersent();
			String tagStatus = filteredTag.getCalibrationStatus();
			String tagReason = filteredTag.getExplanation();
			String tagCamera = filteredTag.getCameraIp();
			String tagLevelId = filteredTag.getLevelId();
			String tagTotalId = filteredTag.getTotalId();

			//Из полученных данных берём результаты
			Taglog taglog = null;
			//Смотрим что мы вообще искали и соответственно выбираем с чем сравнивать
			switch (requestType) {
			case "level":
				RequestColumn = filteredTag.getLevelId();
				break;
			case "total":
				RequestColumn = filteredTag.getTotalId();
				break;
			default:
				RequestColumn = filteredTag.getOnlineId();
			}
			//Нужна именно финальная переменная
			final String finalRequestColumn = RequestColumn;

			try {
				taglog = latestLogs.stream()
						.filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(finalRequestColumn)).findFirst()
						.orElse(null);
				//Если нам в этом же месте был нужен ещё и уровень воды
				if (Level) {
					LevelObj = latestLogsLevel.stream()
							.filter(g -> String.valueOf(g.getTag_id()).equals(filteredTag.getLevelId())).findFirst()
							.orElse(null);

					if (LevelObj != null && LevelObj.getData_value() != null) {
						LevelValue = LevelObj.getData_value().toString();
					} else {
						LevelValue = null;
					}

				}

			} catch (Exception e) {
				//СЮДА ВСТАВИТЬ ЛОГИРОВАНИЕ
				e.printStackTrace();
				continue;
			}

			//Ещё раз проверяем есть ли у нас объект
			if (taglog == null)
				continue;
			TagLogWithName tagLogWithName = new TagLogWithName(taglog, tagName, tagText, LevelValue,tagLevelId, tagTotalId, tagStatus,
					tagReason, tagCamera);
			tagLogsWithNames.add(tagLogWithName);

		}

		return tagLogsWithNames;
	}

	// Отправка всего нужного на модель
	public void setTagLogPageInModel(List<TagLogWithName> tagLogsWithNames, int page, Model model, String pagename,
			int measurement) {
		int size = 1000;
		Pageable pageable = PageRequest.of(page, size);
		Page<TagLogWithName> tagLogPage = new PageImpl<>(tagLogsWithNames, pageable, tagLogsWithNames.size());
		model.addAttribute("tagLogPage", tagLogPage);
		model.addAttribute("pagename", pagename);
		model.addAttribute("measurement", measurement);
	}

	// Форматирование чисел результатов(слишком длинные)
	public void formatDataValues(List<TagLogWithName> tagLogsWithNames, boolean forValue, boolean forLevel) {
		for (TagLogWithName tagLogWithName : tagLogsWithNames) {
			if (tagLogWithName.getData_value() != null && forValue) {
				Float dataValue = Math.round(tagLogWithName.getData_value() * 100.0f) / 100.0f;
				tagLogWithName.setData_value(dataValue);

			}

			if (tagLogWithName.getTagLevel() != null && forLevel && tagLogWithName.getTagLevel() != "") {
				Float dataValueLvl = Math.round(Float.parseFloat(tagLogWithName.getTagLevel()) * 10.0f) / 10.0f;
				tagLogWithName.setTagLevel(dataValueLvl.toString());
			}
		}
	}

	// Проклятый метод, для приравнивания айдишников по проходимости и уровню воды
	public String getLevelTagValue(String mainId, List<TagLogWithName> tagLogsLevel,
			Map<String, Integer> tagToLevelMap) {

		String levelTagId = String.valueOf(mainId);
		System.out.println("Level is: " + levelTagId);

		Optional<TagLogWithName> foundTagLog = tagLogsLevel.stream()
				.filter(tagLogWithName -> tagLogWithName.getTag_id().toString().equals(levelTagId)).findFirst();

		String Level = foundTagLog.map(tagLogWithName -> {
			Float dataValue = tagLogWithName.getData_value();
			return dataValue != null ? dataValue.toString() : "";
		}).orElse("");

		return Level;

	}

	public String formatCurrentMonth() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		return currentDate.format(formatter);
	}

}
