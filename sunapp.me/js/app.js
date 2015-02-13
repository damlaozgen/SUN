(function ($) {
  "use strict;"
  
  $(document).ready(function() {

    // prevent the # links to scroll to the top of the page
    $("[href=#]").click(function(e) {
      e.preventDefault();
    });

    // Sticky header style change on sroll
    $(window).scroll(function() {
      if ($(this).scrollTop() > 100) {
        $('header').addClass('stuck');
      } else {
        $('header').removeClass('stuck');
      }
    });

    // Smooth scroll functionality
    $('.smoothScroll').smoothScroll({
      speed: 600,
      offset: -79
    });

    $('.smoothScrollNoOffset').smoothScroll({
      speed: 600
    });

    // closing menu on mobile after click on item menu
    $(".navbar li a").click(function(event) {
      if ($(window).width() <= 767) {
        $(".navbar-collapse").removeClass("in");
      }
    });

    // Lightbox
    $('.venobox').venobox();

    // Animate elements
    if (jQuery().viewportChecker) {
      $('.animFadeInUp').viewportChecker({
        classToAdd: 'opacity-1 animated fadeInUp',
        offset: 50
       });

      $('.animFadeInLeft').viewportChecker({
        classToAdd: 'opacity-1 animated fadeInLeft',
        offset: 50
       });
    }

    // Revolution Slider
    $('.tp-banner').revolution({
      delay: 9000,
      startwidth: 1170,
      hideCaptionAtLimit: 767,
      fullScreen: "on",
      hideTimerBar: "on",
      hideArrowsOnMobile: "on",
      navigationStyle: "preview4",
      spinner: "spinner4"
    });

    $('.tp-banner-2').revolution({
      delay: 9000,
      startwidth: 1170,
      hideCaptionAtLimit: 767,
      parallax: "scroll",
      parallaxBgFreeze: "off",
      parallaxLevels: [1,5,10],
      hideTimerBar: "on",
      hideArrowsOnMobile: "on",
      navigationStyle: "preview4",
      spinner: "spinner4"
    });

    $('.tp-banner-3').revolution({
      startheight: 700,
      hideCaptionAtLimit: 767,
      hideTimerBar: "on",
      hideArrowsOnMobile: "on",
      navigationStyle: "preview4",
      spinner: "spinner4"
    });

    /* Form submission code */
    // Get the form.
    var form = $('#theme-contact');

    // Get the messages div.
    var formMessages = $('#theme-form-messages');

    // Set up an event listener for the contact form.
    $(form).submit(function(e) {
      // Stop the browser from submitting the form.
      e.preventDefault();

      // Serialize the form data.
      var formData = $(form).serialize();

      // Submit the form using AJAX.
      $.ajax({
        type: 'POST',
        url: $(form).attr('action'),
        data: formData
      })
      .done(function(response) {
        // Set the message text.
        $(formMessages).html(response);

        // Clear the form.
        $('#contact-name').val('');
        $('#contact-email').val('');
        $('#contact-subject').val('');
        $('#contact-message').val('');
      })
      .fail(function(data) {
        // Set the message text.
        if (data.responseText !== '') {
          $(formMessages).html(data.responseText);
        } else {
          $(formMessages).html('<div class="alert alert-danger margin-top-40"><button type="button" class="close" data-dismiss="alert">×</button><strong>Error!</strong><br /> Error!</div>');
        }
      });

    });

  });
  
})(jQuery);

jQuery(window).load(function() {
  "use strict";

  // Google Maps Goodness
  if (document.getElementById('map_canvas')) {
    var gLatitude = 39.869679;
    var gLongitude = 32.749016;
    var gZoom = 13;
    var gTitle = 'Bilkent';
    var gDescription = 'SUN is being developed at Bilkent University';
      
    var latlng = new google.maps.LatLng(gLatitude, gLongitude);
    
    var settings = {
      zoom: parseInt(gZoom),
      center: latlng,
      scrollwheel: false,
      mapTypeControl: true,
      mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
      navigationControl: true,
      navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    var map = new google.maps.Map(document.getElementById("map_canvas"), settings);
    
    var companyLogo = new google.maps.MarkerImage('img/google-maps/marker-flat.png',
                                                  new google.maps.Size(40,40),
                                                  new google.maps.Point(0,0),
                                                  new google.maps.Point(20,40));
    
    var companyMarker = new google.maps.Marker({
      position: latlng,
           map: map,
          icon: companyLogo,
         title: gTitle
    });
    
    var contentString = '<div id="content-map">'+
                          '<h3>' + gTitle + '</h3>'+
                          '<p>' + gDescription + '</p>'+
                        '</div>';
    
    var infowindow = new google.maps.InfoWindow({
      content: contentString
    });
    
    google.maps.event.addListener(companyMarker, 'click', function() {
      infowindow.open(map,companyMarker);
    });
  
  }

});