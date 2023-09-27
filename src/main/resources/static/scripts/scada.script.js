  $(document).ready(function () {
        $('#resultModal').modal('show'); 
    });
    
    
    
    $(document).ready(function() {
	if (!$.fn.DataTable.isDataTable('#tagLogTable')) {
    var table_main = $('#tagLogTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'copy',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'csv',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'excel',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'pdf',
                title: '"SCADA" Anlıq sərfiyyat',
                customize: function (doc) {
					doc.defaultStyle.fontSize = 8;
                    doc.pageMargins = [20, 30, 20, 30]; // Установите отступы страницы по вашему выбору
                    doc.pageSize = 'A4'; // Установите размер страницы (A4 или другой)
                }
            },
            {
                extend: 'print',
                title: '"SCADA" Anlıq sərfiyyat'
            }
        ],
        paging: false,
        fixedHeader: true,
        info: false,
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Axtarış"
        },
         columnDefs: [
            { targets: [4, 5], type: 'num' } // Задаем тип данных 'num' для столбцов 4 и 5
        ]
    });
    };
    
    
    
    
    	/*if (!$.fn.DataTable.isDataTable('#tagLogTable-second')) {
    var table_extra = $('#tagLogTable-second').DataTable({
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'copy',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'csv',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'excel',
                title: '"SCADA" Anlıq sərfiyyat'
            },
            {
                extend: 'pdf',
                title: '"SCADA" Anlıq sərfiyyat',
                customize: function (doc) {
					doc.defaultStyle.fontSize = 8;
                    doc.pageMargins = [20, 30, 20, 30]; // Установите отступы страницы по вашему выбору
                    doc.pageSize = 'A4'; // Установите размер страницы (A4 или другой)
                }
            },
            {
                extend: 'print',
                title: '"SCADA" Anlıq sərfiyyat'
            }
        ],
        paging: false,
        fixedHeader: true,
        info: false,
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Axtarış"
        },
         columnDefs: [
                { targets: [2], type: 'num' }, // Указываем, что третий столбец (с индексом 2) должен сортироваться как числовой
                { targets: [0, 3], orderData: [0, 3, 2] } // Указываем порядок сортировки для второго и пятого столбцов, где может быть "offline"
            ]
    });
    };*/
    
    
    
    
    
  $(document).ready(function() {
    if (!$.fn.DataTable.isDataTable('#tagLogTable-second')) {
        var table_extra = $('#tagLogTable-second').DataTable({
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'copy',
                    title: '"SCADA" Anlıq sərfiyyat'
                },
                {
                    extend: 'csv',
                    title: '"SCADA" Anlıq sərfiyyat'
                },
                {
                    extend: 'excel',
                    title: '"SCADA" Anlıq sərfiyyat'
                },
                {
                    extend: 'pdf',
                    title: '"SCADA" Anlıq sərfiyyat',
                    customize: function (doc) {
                        doc.defaultStyle.fontSize = 8;
                        doc.pageMargins = [20, 30, 20, 30];
                        doc.pageSize = 'A4';
                    }
                },
                {
                    extend: 'print',
                    title: '"SCADA" Anlıq sərfiyyat'
                }
            ],
            paging: false,
            fixedHeader: true,
            info: false,
            language: {
                search: "_INPUT_",
                searchPlaceholder: "Axtarış"
            },
            columnDefs: [
                { targets: [2], type: 'num' },
                { orderData: [2], type: 'num' } // Указываем порядок сортировки для второго и пятого столбцов
            ]
        });
    };
});


    
    
});
// -----
function openModal(tagName, tagTotalId, tagId, camLink) {
	$('#myModal').modal('show'); 
	$('#myModalLabel').text("Ətraflı məlumat - " + tagName);
	$('#statusInfo').text("");
	$('#statusText').text("");
	$('#tagLogId').val(tagTotalId).hide();

	
	// Получите элемент <a> с id "buttonLinkPdf"
    var buttonLinkPdf = $('#buttonLinkPdf');

    // Установите атрибут href для ссылки
    buttonLinkPdf.attr('href', '/get-pdf/' + tagId + '.pdf');

    // Установите атрибут target="_blank", чтобы открыть ссылку в новой вкладке
    buttonLinkPdf.attr('target', '_blank');

    
    

    var buttonLinkCamera = $('#buttonLinkCamera');

    camLink = 'http://' + camLink;
    // Установите атрибут href для ссылки без "localhost" и с протоколом "https://"
    buttonLinkCamera.attr('href', camLink);

    // Установите атрибут target="_blank", чтобы открыть ссылку в новой вкладке
    buttonLinkCamera.attr('target', '_blank');
 
}




$('#myForm').submit(function (e) {
    e.preventDefault(); // Предотвратить обычную отправку формы
    $('#statusText').hide();
    $('#statusInfo').hide();
    $('#loadingAnimation').show();
    
    // Создайте объект данных для отправки на сервер
    var formData = {
        dateRangePicker: $('#dateRangePicker').val(),
        tagLogId: $('#tagLogId').val()
    };

$.ajax({
    type: 'POST',
    url: '/extra-info', 
    data: formData,
    success: function (data) {
        // Обновляем значение лейбла с полученными данными
         $('#statusText').text("Seçilmiş vaxt aralığında keçən toplam su miqdarı: " );
        $('#statusInfo').text(data+" m"+'3\u00B3' );
        $('#statusText').show();
        $('#statusInfo').show();
        $('#loadingAnimation').hide();
    },
    error: function (xhr, status, error) {
        console.error('Ошибка при выполнении POST-запроса: ' + error);
        $('#statusText').show();
        $('#statusText').text("Problem aşkarlandı, inzibatçı ilə əlaqə saxlayın." );
        $('#loadingAnimation').hide();
    }
});
});

function closeModal() {
	$('#myModal').modal('hide');
	 $('#dateRangePicker').val('');
	    $('#tagLogId').val('');
}



$(document).ready(function () {
    // Инициализация поля выбора диапазона дат
    $('#dateRangePicker').daterangepicker({
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 1,
        numberOfMonths: 3, // Отображать три месяца
        minYear:2022,
        locale: {
            format: 'MM/DD/YYYY HH:mm', // Формат даты и времени
            applyLabel: 'Qəbul etmək', // Текст кнопки "Применить"
            cancelLabel: 'Ləğv etmək', // Текст кнопки "Отмена"
            daysOfWeek: ['B.', 'B.E.', 'Ç.A.', 'Ç.', 'C.A.', 'C.', 'Ş'], // Дни недели
            monthNames: ['Yanvar', 'Fevral', 'Mart', 'Aprel', 'May', 'İyun', 'İyul', 'Avqust', 'Sentyabr', 'Oktyabr', 'Noyabr', 'Dekabr'], // Месяцы
            firstDay: 1 // Неделя начинается с понедельника
        },
        startDate: moment().subtract(7, 'days'), // Начальная дата
        endDate: moment(), // Конечная дата (по умолчанию текущая дата и время)
        opens: 'left' // Открывать календарь с левой стороны
    });
});
