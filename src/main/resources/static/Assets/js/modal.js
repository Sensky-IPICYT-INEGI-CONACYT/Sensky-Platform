(()=>{
    const btnReg = document.querySelector('#registrase'),
          btnInit = document.querySelector('#iniciar'),
          btnUser = document.querySelector('#nav-mobile'),
          modalReg = document.querySelector('#modalReg'),
          modalInit = document.querySelector('#modalInit'),
          btnModalReg = document.querySelector('#btnModalReg'),
          overlay = document.querySelector('.overlay'),
          close= document.querySelectorAll('.close');


    const openModalInit = () => {
        modalInit.classList.remove('hide');
        modalInit.classList.add('show');
        modalReg.classList.remove('show');
        modalReg.classList.add('hide');
        overlay.classList.remove('hide');
    }
    const openModalReg = () => {
        modalInit.classList.remove('show');
        modalInit.classList.add('hide');
        modalReg.classList.remove('hide');
        modalReg.classList.add('show');
        overlay.classList.remove('hide');
    }
    const closeModal = () => {
        modalInit.classList.remove('show');
        modalInit.classList.add('hide');
        modalReg.classList.add('hide');
        modalReg.classList.remove('show');
        overlay.classList.add('hide');
    }


    btnReg.addEventListener('click', openModalReg);
    
    btnInit.addEventListener('click', openModalInit);
    
    btnModalReg.addEventListener('click', openModalReg);

    close.forEach(btn => {
        btn.addEventListener('click', closeModal);
    });


    overlay.addEventListener('click', closeModal);

    
    btnUser.addEventListener('click', openModalInit);

    //simplificar en una funcion 

})()