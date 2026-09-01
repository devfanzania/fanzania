// JavaScript Document
jQuery(document).ready(function($){
	
    //window.onload = function () {
    //    $('.slider').slick({
    //        autoplay: false,
    //        autoplaySpeed: 1500,
    //        arrows: true,
    //        prevArrow: '<button type="button" class="slick-prev"></button>',
    //        nextArrow: '<button type="button" class="slick-next"></button>',
    //        centerMode: true,
    //        slidesToShow: 1,
    //        slidesToScroll: 1
    //    });
    //};
    /*==========Banner Slider===========*/
    //$('.t-slider').slick({
    //    autoplay: false,
    //    autoplaySpeed: 1500,
    //    arrows: true,
    //    prevArrow: '<button type="button" class="slick-prev"></button>',
    //    nextArrow: '<button type="button" class="slick-next"></button>',
    //    centerMode: true,
    //    slidesToShow: 4,
    //    slidesToScroll: 1
    //});
/*scroll tab*/
var hidWidth;
var scrollBarWidths = 40;

var widthOfList = function(){
  var itemsWidth = 0;
  $('.list li').each(function(){
    var itemWidth = $(this).outerWidth();
    itemsWidth+=itemWidth;
  });
  return itemsWidth;
};

var widthOfHidden = function(){
  return (($('.wrapper').outerWidth())-widthOfList()-getLeftPosi())-scrollBarWidths;
};

var getLeftPosi = function(){
  return $('.list').position().left;
};

var reAdjust = function(){
  if (($('.wrapper').outerWidth()) < widthOfList()) {
    $('.scroller-right').show();
  }
  else {
    $('.scroller-right').hide();
  }
  
  //if (getLeftPosi()<0) {
  //  $('.scroller-left').show();
  //}
  //else {
  //  $('.item').animate({left:"-="+getLeftPosi()+"px"},'slow');
  //	$('.scroller-left').hide();
  //}
}

reAdjust();

$(window).on('resize',function(e){  
  	reAdjust();
});

$('.scroller-right').click(function() {
  
  $('.scroller-left').fadeIn('slow');
  $('.scroller-right').fadeOut('slow');
  
  $('.list').animate({left:"+="+widthOfHidden()+"px"},'slow',function(){

  });
});

$('.scroller-left').click(function() {
  
	$('.scroller-right').fadeIn('slow');
	$('.scroller-left').fadeOut('slow');
  
  	$('.list').animate({left:"-="+getLeftPosi()+"px"},'slow',function(){
  	
  	});
});

});