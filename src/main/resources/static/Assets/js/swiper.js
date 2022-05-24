 var swiper = new Swiper('.swiper-container', {
        effect: 'coverflow',
        grabCursor: true,
        centeredSlides: true,
        slidesPerView: 'auto',
        coverflowEffect: {
          rotate: 25,
          stretch: 0,
          depth: 400,
          modifier: 1,
          slideShadows: true,
        },
        loop: true,
        autoplay: {
          delay: 3500,
          disableOnInteraction: false,
        },
        navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
      },
      });