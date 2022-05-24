let navegationScroll = () => {
    const navbar = document.querySelector("nav");
    const navbarHeight = navbar.clientHeight;
    const referenceElement = document.querySelector(".switch");

  
    const checkPosition = ()=> {
        const Y_position = referenceElement.getBoundingClientRect().top;
        const distance = (((navbarHeight*100) / Y_position)  ).toFixed(0);
        if (distance > 0 ) {
            if (distance >= 100) {
                navbar.style.background= `rgba(6, 48, 92, 1)`
            }else  if (distance > 0 && distance < 10 ){
                navbar.style.background= 'none'
            }
            else{
            navbar.style.background= `rgba(6, 48, 92, 0.${distance})`
            }
        }
    }

    window.addEventListener('scroll', checkPosition);

}

navegationScroll()