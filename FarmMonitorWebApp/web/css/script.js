( function( $ ) {
$( document ).ready(function() {
   
    $('#cssmenu ul ul li:odd').addClass('odd');
    $('#cssmenu ul ul li:even').addClass('even');

    $('#cssmenu > ul > li > a').click(function()
    {
        /*$('#cssmenu li').removeClass('active');
        $(this).closest('li').addClass('active');*/	
        var checkElement = $(this).next();
        if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
            //$(this).closest('li').removeClass('active');
           checkElement.slideUp('normal');
        }
        if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
            $('#cssmenu ul ul:visible').slideUp('normal');
            checkElement.slideDown('normal');
        }
        if($(this).closest('li').find('ul').children().length == 0) {
            return true;
        } else {
            return false;	
        }		
});
});
 
} )( jQuery );

function makeactive(num) {

    $.each([ 1, 2, 3, 4, 5], function(index, value ) {
     var li = "#li" + value;
     if(value === num)
     {
         $(li).addClass('active');         
     }
     else
     {
         if($(li).hasClass('active'))
            $(li).removeClass('active');
     }
    });     
 }