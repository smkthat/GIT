$(document).ready(function(){

    $('.button').click(function(){
        $(this).toggleClass('active');
        $('.block').toggleClass('opener');
        if (!$(this).data('status')) {
            $(this).data('status', true).html('hide');
        } else {
            $(this).data('status', false).html('show');
        }
    });

});