$(window).on('load', function() {
    $('.preloader-wrapper').fadeOut();
    $('body').removeClass('preloader-site');
});

$(document).ready(function() {
    var body = $('body');
        body.addClass('preloader-site');

    $('.truncate').succinct({
        size: 30,
        ignore: false
    });

    $('.more-content li').succinct({
        size: 10000,
        ignore: false
    });

    var scrollup = $('.scrollup-button');
    $(window).scroll(function() {
        if ($(window).scrollTop() > 100) {
            scrollup.addClass('show');
        } else {
            scrollup.removeClass('show');
        }
    });

    scrollup.on('click', function(e) {
        e.preventDefault();
        $('html, body').animate({
            scrollTop:0
        }, '100');
    });

});

var speed = 100;
var originalHeight = 30;
$('.content').hover(function() {
    var hoverHeight = 60 + $(this).find('.more-content li').height();
    $(this).stop().animate({
        height:hoverHeight
    }, speed);
},function(){
    $(this).stop().animate({
        height:originalHeight
    }, speed);
})

$('.cb-value').click(function() {
    var mainParent = $(this).parent('.toggle-btn');
    if($(mainParent).find('input.cb-value').is(':checked')) {
        $(mainParent).addClass('active');
    } else {
        $(mainParent).removeClass('active');
    }
});
