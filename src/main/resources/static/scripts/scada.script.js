  $(document).ready(function () {
        $('#resultModal').modal('show'); 
    });
    
    

$(document).ready(function() {
	if (!$.fn.DataTable.isDataTable('#tagLogTable')) {
		if (!$.fn.DataTable.isDataTable('#tagLogTable')) {
			$.fn.dataTable.ext.order['custom'] = function(settings, col) {
				return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
					var cellValue = $(td).text();
					cellValue = (cellValue === 'offline') ? 'ZZZZZZZZZ' : cellValue; // Присваиваем значение переменной cellValue в соответствии с условием
					return cellValue;
				});
			};

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
						customize: function(doc) {
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
					searchPlaceholder: "Axtarış..."
				},
				columnDefs: [
					{

						targets: [3, 4],
						type: 'num',
						render: function(data, type, row) {

							if (type === 'sort') {
								// Удалите все символы, кроме цифр и точек (для десятичных чисел)
								var numericData = data.replace(/[^0-9.]/g, '');
								var parsedData = parseFloat(numericData);

								return parsedData;
							}
							return data;
						}
					}
				],
				order: [[0, 'asc']], // Нумерация будет в порядке возрастания
            orderSequence: ['asc'], // Управляет порядком сортировки
select: true, // Включаем плагин Select

initComplete: function () {
    this.api().columns([2, 7]).every(function (colIdx) {
        var column = this;
        var defaultValue = colIdx === 2 ? 'Bütün rayonal' : 'Bütün statuslar';
        var select = $('<select><option value="" selected>' + defaultValue + '</option></select>')
            .appendTo($(column.header()))
            .on('change', function () {
                var val = $.fn.dataTable.util.escapeRegex($(this).val());
                column.search(val ? '^' + val + '$' : '', true, false).draw();
            });
        column.data().unique().sort().each(function (d, j) {
            select.append('<option value="' + d + '">' + d + '</option>');
        });
                });
            }
        });
        
        // Добавляем колонку с нумерацией строк
        table_main.on('order.dt search.dt', function() {
            table_main.column(0, {search: 'applied', order: 'applied'}).nodes().each(function(cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();
        
    }
    
};
    
    
    
    
    
    //-------------------------------
    
    
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
            {
	 
	targets: [2], 
	type: 'num' ,
	render: function (data, type, row) {
            if (type === 'sort') {
                // Удалите все символы, кроме цифр и точек (для десятичных чисел)
                var numericData = data.replace(/[^0-9.]/g, '');
                var parsedData = parseFloat(numericData);
                return parsedData;
            }
            return data;
        }
	} 
        ]
        
    });
    };
  
  
    
})






$(document).ready(function () {
        if (!$.fn.DataTable.isDataTable('#WellTable')) {
            var table_well = $('#WellTable').DataTable({
            	
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
                    {
                        targets: [2],
                        type: 'string',
                        render: function (data, type, row) {
                            if (type === 'sort') {
                                var numericData = data.replace(/[^0-9.]/g, '');
                                var parsedData = parseFloat(numericData);
                                return parsedData;
                            }
                            return data;
                        }
                    }
                ],
                

                
          
		select: true, 
   
    
    initComplete: function () {
        this.api().columns([3, 4]).every(function (colIdx) {
            var column = this;
            var defaultValue = colIdx === 3 ? 'Bütün rayonal' : 'Bütün statuslar';
            var select = $('<select><option value="" selected>' + defaultValue + '</option></select>')
                .appendTo($(column.header()))
                .on('change', function () {
                    var val = $.fn.dataTable.util.escapeRegex($(this).val());
                    column.search(val ? '^' + val + '$' : '', true, false).draw();
                });
            column.data().unique().sort().each(function (d, j) {
                select.append('<option value="' + d + '">' + d + '</option>');
            });
                    });
                },
                
   
                
            });
            // Добавляем колонку с нумерацией строк
            table_well.on('order.dt search.dt', function() {
                table_well.column(0, {search: 'applied', order: 'applied'}).nodes().each(function(cell, i) {
                    cell.innerHTML = i + 1;
                });
            }).draw();
        }
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


// Функция для прокрутки страницы наверх

function scrollToTop() {
	
	var i =0;
	
    const currentY = window.scrollY;
    const step = Math.max(currentY / 25, 20); // Настройте скорость прокрутки по вашему усмотрению

   function scrollStep() {
        if (window.scrollY > 0) {

	 
            window.scrollTo(0, window.scrollY - step);
          
            if(i===window.scrollY){

	 window.scrollTo({
        top: 0,
        behavior: "smooth" // Плавная прокрутка
    });
	
}
    else
    {
			i=window.scrollY;
			
	     requestAnimationFrame(scrollStep);}
            
        } 
    }
    requestAnimationFrame(scrollStep);
    
    
    
}



// Отслеживаем прокрутку страницы
window.addEventListener("scroll", function () {
    var scrollToTopButton = document.getElementById("scrollToTopButton");

    // Показываем или скрываем кнопку "Наверх" в зависимости от положения прокрутки
    if (window.pageYOffset > 200) {
        scrollToTopButton.style.display = "block";

    } else {
        scrollToTopButton.style.display = "none";

    }
});
