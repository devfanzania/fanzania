var video = document.getElementById("myVideo");
var btn = document.getElementById("myBtn");

//$(document).ready(function () {
//    $('#menu > ul > li > a').click(function () {
//        $('#menu > ul > li > a').removeClass("current");
//        $(this).addClass("current");
//    });
//});

function myFunction() {
  if (video.paused) {
    video.play();
    btn.innerHTML = "Pause";
  } else {
    video.pause();
    btn.innerHTML = "Play";
  }
}
	//Update Scroll to Top
	function headerStyle() {
		if($('.main-header').length){
			var windowpos = $(window).scrollTop();
			if (windowpos >= 200) {
				$('.main-header').addClass('fixed-header');
				$('.scroll-to-top').fadeIn(300);
			} else {
				$('.main-header').removeClass('fixed-header');
				$('.scroll-to-top').fadeOut(300);
			}
		}
	}
	
	headerStyle();
	
	//Submenu Dropdown Toggle
	if($('.main-header li.dropdown ul').length){
		$('.main-header li.dropdown').append('<div class="dropdown-btn"></div>');
		
		//Dropdown Button
		$('.main-header li.dropdown .dropdown-btn').on('click', function() {
			$(this).prev('ul').slideToggle(500);
		});
		
		//Disable dropdown parent link
		$('.navigation li.dropdown > a').on('click', function(e) {
			e.preventDefault();
		});
	}
	
	//Search Box Toggle
	if($('.main-header .seach-toggle').length){
		//Dropdown Button
		$('.main-header .seach-toggle').on('click', function() {
			$(this).next('.search-box').toggleClass('now-visible');
		});
	}
	
	//Revolution Slider Style One
	if($('.main-slider.default-style .tp-banner').length){

		jQuery('.main-slider.default-style .tp-banner').show().revolution({
		  delay:10000,
		  startwidth:1200,
		  startheight:600,
		  hideThumbs:600,
	
		  thumbWidth:80,
		  thumbHeight:50,
		  thumbAmount:5,
	
		  navigationType:"bullet",
		  navigationArrows:"0",
		  navigationStyle:"preview3",
	
		  touchenabled:"on",
		  onHoverStop:"off",
	
		  swipe_velocity: 0.7,
		  swipe_min_touches: 1,
		  swipe_max_touches: 1,
		  drag_block_vertical: false,
	
		  parallax:"mouse",
		  parallaxBgFreeze:"on",
		  parallaxLevels:[7,4,3,2,5,4,3,2,1,0],
	
		  keyboardNavigation:"off",
	
		  navigationHAlign:"center",
		  navigationVAlign:"bottom",
		  navigationHOffset:0,
		  navigationVOffset:40,
	
		  soloArrowLeftHalign:"left",
		  soloArrowLeftValign:"center",
		  soloArrowLeftHOffset:20,
		  soloArrowLeftVOffset:0,
	
		  soloArrowRightHalign:"right",
		  soloArrowRightValign:"center",
		  soloArrowRightHOffset:20,
		  soloArrowRightVOffset:0,
	
		  shadow:0,
		  fullWidth:"on",
		  fullScreen:"off",
	
		  spinner:"spinner4",
	
		  stopLoop:"off",
		  stopAfterLoops:-1,
		  stopAtSlide:-1,
	
		  shuffle:"off",
	
		  autoHeight:"off",
		  forceFullWidth:"on",
	
		  hideThumbsOnMobile:"on",
		  hideNavDelayOnMobile:1500,
		  hideBulletsOnMobile:"on",
		  hideArrowsOnMobile:"on",
		  hideThumbsUnderResolution:0,
	
		  hideSliderAtLimit:0,
		  hideCaptionAtLimit:0,
		  hideAllCaptionAtLilmit:0,
		  startWithSlide:0,
		  videoJsPath:"",
		  fullScreenOffsetContainer: ""
	  });
		
	}
	
	
	//$('#menu > ul.navigation li a').click(function (e) {
	//    var $this = $(this);
	//    $this.parent().siblings().removeClass('current').end();
	//    $this.parent().addClass('current');
	//    if ($this.attr('href').indexOf(current) !== -1) {
	//        $this.addClass('current');
	//    }
	//    e.preventDefault();
	//});

	//$(function () {
	//    var current = location.pathname;
	//    $('#menu li a').each(function () {
	//        var $this = $(this);
	//           $this.parent().siblings().removeClass('current').end();
	//         if the current path is like this link, make it active
	//        if ($this.attr('href').indexOf(current) !== -1) {
	//            $this.addClass('current');
	//        }
	//    })
//})

	
	//Revolution Slider Fullscreen
	if($('.main-slider.fullscreen .tp-banner').length){

		jQuery('.main-slider.fullscreen .tp-banner').show().revolution({
		  delay:10000,
		  startwidth:1200,
		  startheight:900,
		  hideThumbs:600,
	
		  thumbWidth:80,
		  thumbHeight:50,
		  thumbAmount:5,
	
		  navigationType:"bullet",
		  navigationArrows:"0",
		  navigationStyle:"preview3",
	
		  touchenabled:"on",
		  onHoverStop:"off",
	
		  swipe_velocity: 0.7,
		  swipe_min_touches: 1,
		  swipe_max_touches: 1,
		  drag_block_vertical: false,
	
		  parallax:"mouse",
		  parallaxBgFreeze:"on",
		  parallaxLevels:[7,4,3,2,5,4,3,2,1,0],
	
		  keyboardNavigation:"off",
	
		  navigationHAlign:"center",
		  navigationVAlign:"bottom",
		  navigationHOffset:0,
		  navigationVOffset:20,
	
		  soloArrowLeftHalign:"left",
		  soloArrowLeftValign:"center",
		  soloArrowLeftHOffset:20,
		  soloArrowLeftVOffset:0,
	
		  soloArrowRightHalign:"right",
		  soloArrowRightValign:"center",
		  soloArrowRightHOffset:20,
		  soloArrowRightVOffset:0,
	
		  shadow:0,
		  fullWidth:"on",
		  fullScreen:"off",
	
		  spinner:"spinner4",
	
		  stopLoop:"off",
		  stopAfterLoops:-1,
		  stopAtSlide:-1,
	
		  shuffle:"off",
	
		  autoHeight:"off",
		  forceFullWidth:"on",
	
		  hideThumbsOnMobile:"on",
		  hideNavDelayOnMobile:1500,
		  hideBulletsOnMobile:"on",
		  hideArrowsOnMobile:"on",
		  hideThumbsUnderResolution:0,
	
		  hideSliderAtLimit:0,
		  hideCaptionAtLimit:0,
		  hideAllCaptionAtLilmit:0,
		  startWithSlide:0,
		  videoJsPath:"",
		  fullScreenOffsetContainer: ""
	  });
		
	}

	//Clients SliderThree Column
	if ($('.clients-slider').length) {
		$('.clients-slider').owlCarousel({
			loop:true,
			margin:50,
			nav:false,
			dots:false,
			smartSpeed: 500,
			autoplay: 5000,
			responsive:{
				0:{
					items:2
				},
				600:{
					items:2
				},
				1024:{
					items:4
				},
				1200:{
					items:4
				}
			}
		});
	}
	
	
	//Testimonials Carousel Two
	//if ($('.testimonial-carousel-two').length) {
	//	$('.testimonial-carousel-two').each(function() {
    //            var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
    //            var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
    //            var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
    //            $(this).owlCarousel({
    //                autoplay: false,
    //                autoplayTimeout: data_duration,
    //                loop: true,
    //                items: 2,
    //                margin: 15,
    //                dots: data_dots,
    //                nav: data_nav,
    //                navText: [
    //                    '<i class="fa fa-long-arrow-left"></i>',
    //                    '<i class="fa fa-long-arrow-right"></i>'
    //                ],
    //                responsive: {
    //                    0: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    480: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    600: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    750: {
    //                        items: 2,
    //                        center: false
    //                    },
    //                    960: {
    //                        items: 2
    //                    },
    //                    1170: {
    //                        items: 2
    //                    },
    //                    1300: {
    //                        items: 2
    //                    }
    //                }
    //            });
    //        });   		
	//}
	// Carousel Col 3
	//if ($('.carousel-col-3').length) {
	//	$('.carousel-col-3').each(function() {
    //            var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
    //            var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
    //            var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
    //            $(this).owlCarousel({
    //                autoplay: false,
    //                autoplayTimeout: data_duration,
    //                loop: true,
    //                items: 2,
    //                margin: 15,
    //                dots: data_dots,
    //                nav: data_nav,
    //                navText: [
    //                    '<i class="fa fa-long-arrow-left"></i>',
    //                    '<i class="fa fa-long-arrow-right"></i>'
    //                ],
    //                responsive: {
    //                    0: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    480: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    600: {
    //                        items: 1,
    //                        center: false
    //                    },
    //                    750: {
    //                        items: 2,
    //                        center: false
    //                    },
    //                    960: {
    //                        items: 3
    //                    },
    //                    1170: {
    //                        items: 3
    //                    },
    //                    1300: {
    //                        items: 3
    //                    }
    //                }
    //            });
    //        });   		
	//}
	
	// Fact Counter
	function factCounter() {
		if($('.fact-counter').length){
			$('.fact-counter .counter-column.animated').each(function() {
		
				var $t = $(this),
					n = $t.find(".count-text").attr("data-stop"),
					r = parseInt($t.find(".count-text").attr("data-speed"), 10);
					
				if (!$t.hasClass("counted")) {
					$t.addClass("counted");
					$({
						countNum: $t.find(".count-text").text()
					}).animate({
						countNum: n
					}, {
						duration: r,
						easing: "linear",
						step: function() {
							$t.find(".count-text").text(Math.floor(this.countNum));
						},
						complete: function() {
							$t.find(".count-text").text(this.countNum);
						}
					});
				}
				
			});
		}
	}
	
	//Progress Bar / Levels
	if($('.progress-levels .progress-box .bar-fill').length){
		$(".progress-box .bar-fill").each(function() {
			var progressWidth = $(this).attr('data-percent');
			$(this).css('width',progressWidth+'%');
			$(this).children('.percent').html(progressWidth+'%');
		});
	}
	
	//LightBox / Fancybox
	if($('.lightbox-image').length) {
		$('.lightbox-image').fancybox({
			openEffect  : 'elastic',
			closeEffect : 'elastic',
			helpers : {
				media : {}
			}
		});
	}
	
	
	//Contact Form Validation
	if($('#contact-form').length){
		$('#contact-form').validate({
			rules: {
				username: {
					required: true
				},
				lastname: {
					required: true
				},
				email: {
					required: true,
					email: true
				},
				phone: {
					required: true
				},
				message: {
					required: true
				}
			}
		});
	}
	
	// Scroll to a Specific Div
	if($('.scroll-to-target').length){
		$(".scroll-to-target").on('click', function() {
			var target = $(this).attr('data-target');
		   // animate
		   $('html, body').animate({
			   scrollTop: $(target).offset().top
			 }, 1000);
	
		});
	}
	
	
	// Elements Animation
	if($('.wow').length){
		var wow = new WOW(
		  {
			boxClass:     'wow',      // animated element css class (default is wow)
			animateClass: 'animated', // animation css class (default is animated)
			offset:       0,          // distance to the element when triggering the animation (default is 0)
			mobile:       true,       // trigger animations on mobile devices (default is true)
			live:         true       // act on asynchronously loaded content (default is true)
		  }
		);
		wow.init();
	}

// ParticlesJS Config.
particlesJS("particles-js", {
  "particles": {
    "number": {
      "value": 80,
      "density": {
        "enable": true,
        "value_area": 700
      }
    },
    "color": {
      "value": "#ffffff"
    },
    "shape": {
      "type": "circle",
      "stroke": {
        "width": 0,
        "color": "#000000"
      },
      "polygon": {
        "nb_sides": 5
      },
    },
    "opacity": {
      "value": 0.5,
      "random": false,
      "anim": {
        "enable": false,
        "speed": 1,
        "opacity_min": 0.1,
        "sync": false
      }
    },
    "size": {
      "value": 3,
      "random": true,
      "anim": {
        "enable": false,
        "speed": 40,
        "size_min": 0.1,
        "sync": false
      }
    },
    "line_linked": {
      "enable": true,
      "distance": 150,
      "color": "#ffffff",
      "opacity": 0.4,
      "width": 1
    },
    "move": {
      "enable": true,
      "speed": 6,
      "direction": "none",
      "random": false,
      "straight": false,
      "out_mode": "out",
      "bounce": false,
      "attract": {
        "enable": false,
        "rotateX": 600,
        "rotateY": 1200
      }
    }
  },
  "interactivity": {
    "detect_on": "canvas",
    "events": {
      "onhover": {
        "enable": true,
        "mode": "grab"
      },
      "onclick": {
        "enable": true,
        "mode": "push"
      },
      "resize": true
    },
    "modes": {
      "grab": {
        "distance": 140,
        "line_linked": {
          "opacity": 1
        }
      },
      "bubble": {
        "distance": 400,
        "size": 40,
        "duration": 2,
        "opacity": 8,
        "speed": 3
      },
      "repulse": {
        "distance": 200,
        "duration": 0.4
      },
      "push": {
        "particles_nb": 4
      },
      "remove": {
        "particles_nb": 2
      }
    }
  },
  "retina_detect": true
});


         /* ---------------------------------------------
         popup link
         --------------------------------------------- */


        //$('.popup-link').magnificPopup({
        //    type: 'image'
        //    // other options
        //});


        //$('.popup-youtube, .popup-vimeo, .popup-gmaps').magnificPopup({
        //    disableOn: 700,
        //    type: 'iframe',
        //    mainClass: 'mfp-fade',
        //    removalDelay: 160,
        //    preloader: false,
        //    fixedContentPos: false
        //});
