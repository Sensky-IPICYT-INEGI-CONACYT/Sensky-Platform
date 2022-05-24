(()=>{
const login = document.querySelector("#login"),
      loginInputs= login.getElementsByTagName("input"),
      loginErrors = document.querySelectorAll("#login .error"),
      signUp = document.querySelector("#signUp"),
      signUpInputs= signUp.getElementsByTagName("input"),
      signUpErrors = document.querySelectorAll("#signUp .error"),
      subscribe= document.querySelector("#subscribe"),
      [subscribeInputs]= subscribe.getElementsByTagName("input"),
      [subscribeErrors] = document.querySelectorAll("#subscribe .error");

const passInput = document.querySelectorAll('input[type=password]'),
      togglePassword =  document.querySelectorAll(".togglePassword");


    console.log(togglePassword)
      

  subscribe.addEventListener('submit', (event)=>{
    
    if ( !subscribeInputs.validity.valid) {
      event.preventDefault();
      if (!subscribeErrors.classList.contains('activo')) {
        subscribeErrors.classList.add('activo');
      }
      showError(subscribeInputs, subscribeErrors);
    }
  })

  for (let i = 0; i < passInput.length; i++) {

    togglePassword[i].addEventListener("click", function (event) {
      const type = passInput[i].getAttribute('type') === 'password' ? 'text' : 'password';
      passInput[i].setAttribute('type', type);
      this.classList.toggle('fa-eye');
      this.classList.toggle('fa-eye-slash');
    });

  }


  for (let i = 0; i < loginInputs.length; i++) {

      loginInputs[i].addEventListener("focusout", function (event) {
        
        loginInputs[i].classList.remove('error-shake');
    
        if (loginInputs[i].validity.valid) {
          loginErrors[i].innerHTML = "";
          loginErrors[i].className = "error";
        }else {
          showError(loginInputs[i], loginErrors[i] );
        }
      });

      loginInputs[i].addEventListener("focusin", function (event) {
        
        loginErrors[i].classList.remove('activo');
    
        
      });

  }
  for (let i = 0; i < signUpInputs.length; i++) {

    signUpInputs[i].addEventListener("focusout", function (event) {
      signUpInputs[i].classList.remove('error-shake');
  
      if (signUpInputs[i].validity.valid) {
        signUpErrors[i].innerHTML = "";
        signUpErrors[i].classList.remove = "activo";
      }else {
        showError(signUpInputs[i], signUpErrors[i]  );
      }
    });

    if (signUpInputs[i].type === 'checkbox') {
      signUpInputs[i].addEventListener('input', ()=> {
      if(signUpErrors[i].classList.contains('activo')) signUpErrors[i].classList.toggle('activo');
      })
    }

    signUpInputs[i].addEventListener("focusin", function (event) {
      if(signUpErrors[i].classList.contains('activo') && signUpInputs[i].type !== 'checkbox') signUpErrors[i].classList.toggle('activo');
      
    });

  }



  function showError(input, error) {
    error.classList.add("activo");

    switch (input.type) {
      case 'email':
        if (input.validity.valueMissing) {
          error.textContent =
            "Campo obligatorio";
        } else if (input.validity.typeMismatch) {
          error.textContent =
            "Introduce una direccion valida";
        }  else if (input.validity.tooShort) {
          error.textContent =
            "El correo electrónico debe tener al menos ${ email.minLength } caracteres; ha introducido ${ email.value.length }.";
        }
        break;
      case 'password':
        
        if (input.validity.valueMissing) {
          error.textContent =
            "Campo obligatorio";
        } else if (input.value === '') {
          error.textContent =
            "Campo obligatorio";
        } else if (input.value < 8) {
          error.textContent =
            "La contraseña debe tener min 8 digitos";
        }
        break;
      case 'text':
        if (input.validity.valueMissing) {
          error.textContent =
            "Campo obligatorio";
        } 
        break;
      case 'checkbox':
        if (!input.checked) {
          error.textContent =
            "Campo requerido";
        } 
        break;
    
      default:
        error.classList.add = "activo";
        break;
    }

  }


  login.addEventListener("submit", function (event) {
    for (let i = 0; i < loginInputs.length; i++) {
      loginInputs[i].classList.remove('error-shake');
    
      if ( !loginInputs[i].validity.valid) {
        event.preventDefault();
        if (!loginErrors[i].classList.contains('activo')) {
          loginErrors[i].classList.add('activo');
        }
        loginInputs[i].classList.add('error-shake');
        showError(loginInputs[i], loginErrors[i]);
      }
    
    }
  });
  
  signUp.addEventListener("submit", function (event) {
    for (let i = 0; i < signUpInputs.length; i++) {
      signUpInputs[i].classList.remove('error-shake');
    
      if ( !signUpInputs[i].validity.valid) {
        event.preventDefault();
        signUpErrors[i].classList.add('activo');
        signUpInputs[i].classList.add('error-shake');
        showError(signUpInputs[i], signUpErrors[i]);
      }
      if ( signUpInputs[i].type === 'checkbox' && !signUpInputs[i].checked) {
        event.preventDefault();
        signUpErrors[i].classList.add('activo');
        signUpInputs[i].classList.add('error-shake');
        showError(signUpInputs[i], signUpErrors[i]);
      }
    
    }
  });





})()
