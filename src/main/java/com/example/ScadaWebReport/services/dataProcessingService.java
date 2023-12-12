package com.example.ScadaWebReport.services;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.ScadaWebReport.Entity.Mongo.StaticInfoModel;
import com.example.ScadaWebReport.Entity.Mongo.StaticInfoWellModel;
import com.example.ScadaWebReport.Entity.Taglog.Taglog;
import com.example.ScadaWebReport.controllers.TagLogController;
import com.example.ScadaWebReport.repos.StaticInfoRepo;
import com.example.ScadaWebReport.repos.StaticInfoWellRepo;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.repos.VisitorRepo;


@Service
public class dataProcessingService {
	
	private static final Logger log = LoggerFactory.getLogger(TagLogController.class);
	private TaglogRepositoryImpl taglogRepositoryImpl;
	private final StaticInfoRepo staticInfoRepo;
	private final StaticInfoWellRepo staticInfoWellRepository;
	private final VisitorRepo visitorRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	

	@Autowired
	public dataProcessingService(
			TaglogRepo taglogRepo,
			TaglogRepositoryImpl taglogRepositoryImpl,
			StaticInfoRepo staticInfoRepo,
			StaticInfoWellRepo staticInfoWellRepository,
			VisitorRepo visitorRepo) {
		this.taglogRepositoryImpl = taglogRepositoryImpl;
		this.staticInfoRepo = staticInfoRepo;
		this.staticInfoWellRepository = staticInfoWellRepository;
		this.visitorRepo = visitorRepo;

	}

	
	public void UpdateVisitors(String ip)
	{
		visitorRepo.saveOrUpdateVisitor(ip);
	}
	
	//Total number of visitors 
	public Long totalVisitors()
	{return visitorRepo.countAll(); }
	
	public int totalWeekVisitors()
	{
		Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);
		
			Instant currentTime = Instant.now();

			return visitorRepo.countRecentVisitors(sevenDaysAgo, currentTime);//, currentTime); 

		
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

	
	//Временный метод для подбора данных из базы
	public List<Taglog> getLatestLogsForWells(String typeOfSearch, String additionalFilter) {

		List<String> filteredTagIds = null;
		List<StaticInfoWellModel> filteredTags = staticInfoWellRepository.findAll();

		//Смотрим, какие данные мы ищем
		switch (typeOfSearch) {
		case "1":
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getMotorStatusId).collect(Collectors.toList());
			break;
		case "2":
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getLastRunId).collect(Collectors.toList());
			break;
		case "3":
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getCurrentFlowId).collect(Collectors.toList());
			break;
		case "4":
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getTotalFlowId).collect(Collectors.toList());
			break;
		case "5":
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getPowerUsageTotalId).collect(Collectors.toList());
			break;
		default:
			filteredTagIds = filteredTags.stream().map(StaticInfoWellModel::getMotorStatusId).collect(Collectors.toList());
		}

		if(filteredTagIds.equals(null))
			System.out.println("PLOHO");
		
		
		// Преобразуйте список ID тегов в строку с разделителями
		String tagIdString1 = String.join(",", filteredTagIds);

		// Получите последние записи логов для каждого тега(за последний месяц)
		List<Taglog> latestLogs = taglogRepositoryImpl
				.findLatestLogForEachTag("\"tag_log_" + formatCurrentMonth() + "\"", tagIdString1, "");

		// Если в latestLogs меньше строк данных, чем в staticInfoRepo.findAll();,
		// просто заполняем пустыми полям
		if (latestLogs.size() < staticInfoWellRepository.count()) {

			List<String> missingTagIds = filteredTagIds.stream().filter(
					tagId -> !latestLogs.stream().anyMatch(taglog -> taglog.getTag_id().toString().equals(tagId)))
					.collect(Collectors.toList());

			for (String id : missingTagIds) {
				Taglog emptyLog = new Taglog();
				emptyLog.setTag_id(new BigInteger(id));
				latestLogs.add(emptyLog);

			}

		}

	
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
			String tagRegion = filteredTag.getRegion();

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
			 String finalRequestColumn = RequestColumn;

			try {

				
				taglog = latestLogs.stream()
						.filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(finalRequestColumn)).findFirst()
						.orElse(null);
				//Если нам в этом же месте был нужен ещё и уровень воды
				if (Level) {
	
					LevelObj = latestLogsLevel.stream()
							.filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getLevelId())).findFirst()
							.orElse(null);
					if (LevelObj != null && LevelObj.getData_value() != null) {
						LevelValue = LevelObj.getData_value().toString();
					} else {
						LevelValue = null;
					}
					
				}

			} catch (Exception e) {
				 log.error("Error: "+e.getMessage());
				e.printStackTrace();
				continue;
			}

			//Ещё раз проверяем есть ли у нас объект
			if (taglog == null)
				continue;

			TagLogWithName tagLogWithName = new TagLogWithName(taglog, tagName, tagText, LevelValue,tagLevelId, tagTotalId, tagStatus,
					tagReason, tagCamera, tagRegion);
			tagLogsWithNames.add(tagLogWithName);

		}
	
		return tagLogsWithNames;
	}
	
	
	
	
	
	// Тут мы готовим табилцу для колодцев

		public List<Well> getWells( String additionalFilter) {
		
			List<StaticInfoWellModel> filteredTags = staticInfoWellRepository.findAll();// get date from DB
	

			List<Taglog> MotorStatusTagLogs = getLatestLogsForWells("1", additionalFilter);
			List<Taglog> LastRunTagLogs = getLatestLogsForWells("2", additionalFilter);
			List<Taglog> CurrentFlowTagLogs = getLatestLogsForWells("3", additionalFilter);
			List<Taglog> TotalFlowTagLogs = getLatestLogsForWells("4", additionalFilter);
			List<Taglog> PowerUsageTotalTagLogs = getLatestLogsForWells("5", additionalFilter);
		
			//Строим список для отправления в модель
			List<Well> Wells = new ArrayList<>();
			for (StaticInfoWellModel filteredTag : filteredTags) {
				//Если нулл то ничего не делаем и идём к следующему
				if (filteredTag.getTotalFlowId() == null)
					continue;
				
				//Задаём параметры, которые уже под рукой
				 int Id=filteredTag.getId();
				
				String name = filteredTag.getName();
				String coordinates = filteredTag.getCoordinates();
				String region= filteredTag.getRegion();
				String scadaStatus= filteredTag.getScadaStatus();
				String camera = filteredTag.getCameraIp();
				String info = filteredTag.getExplanation();
				
				String motorStatus = Optional.ofNullable(MotorStatusTagLogs.stream()
				        .filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getMotorStatusId()))
				        .findFirst()
				        .orElse(null))
				        .map(tagLog -> Objects.toString(tagLog.getData_value(), ""))
				        .orElse("");

				String lastRun = Optional.ofNullable(LastRunTagLogs.stream()
				        .filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getLastRunId()))
				        .findFirst()
				        .orElse(null))
				        .map(tagLog -> Objects.toString(tagLog.getData_value(), ""))
				        .orElse("");

				
				String currentFlow = Optional.ofNullable(CurrentFlowTagLogs.stream()
				        .filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getCurrentFlowId()))
				        .findFirst()
				        .orElse(null))
				        .map(tagLog -> Objects.toString(tagLog.getData_value(), ""))
				        .orElse("");

				String totalFlow = Optional.ofNullable(TotalFlowTagLogs.stream()
				        .filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getTotalFlowId()))
				        .findFirst()
				        .orElse(null))
				        .map(tagLog -> Objects.toString(tagLog.getData_value(), ""))
				        .orElse("");

				String powerUsageTotal = Optional.ofNullable(PowerUsageTotalTagLogs.stream()
				        .filter(tagLog -> String.valueOf(tagLog.getTag_id()).equals(filteredTag.getPowerUsageTotalId()))
				        .findFirst()
				        .orElse(null))
				        .map(tagLog -> Objects.toString(tagLog.getData_value(), ""))
				        .orElse("");

			
		
				Well newWll = new Well(
						Id,
						name, 
						coordinates,
						region,
						scadaStatus,
						motorStatus,
						lastRun, 
						currentFlow,
						totalFlow,
						powerUsageTotal, 
						camera,
						info
	
						
						);
				Wells.add(newWll);

			}
		
			return Wells;
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

	
	
	//НЕ АКТУАЛЕН
	// Проклятый метод, для приравнивания айдишников по проходимости и уровню воды
	/*public String getLevelTagValue(String mainId, List<TagLogWithName> tagLogsLevel,
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

	}*/
	
	
	  //Вынести это отсюда
		public String actualTable(int month, int year)
		{
			String monthIns=null;
			LocalDate currentDate=null;
			if(month==0 && year==0)
			{
			currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");	 
			String formatCurrentMonth = currentDate.format(formatter);
			return "logs."+"\"tag_log_" + formatCurrentMonth + "\"";
			}
			//Если вдруг дата начала и дата конца в разных месяцах
			else
				if(month<10)
					monthIns="0"+month;
				else 
					monthIns=String.valueOf(month);
			
			
				return	"logs."+"\"tag_log_"+year+"-"+monthIns+ "\"";
		}

	public String formatCurrentMonth() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		return currentDate.format(formatter);
	}

}
