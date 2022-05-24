function despliegaPestana(evento) {
  //Recojo el evento click en el parámetro "evento" y evito que abra el enlace
  evento.preventDefault();

  //Recupero el href del enlace de la pestaña con la palabra this, que hace referencia al elemento que ha lanzado el evento
  var objetivo = this.getAttribute("href");

  //Oculto todos los bloques
  document
  .querySelectorAll(".contenido-pestanas .bloque")
  .forEach(function(elemento) {
    elemento.classList.remove("activo");
  });

  document
  .querySelectorAll(".sistema-pestanas .pestanas a")
  .forEach(function(elemento) {
    elemento.classList.remove("activo");
  });
  
  //Muestro el bloque objetivo
  this.classList.add('activo')
  document.querySelector(objetivo).classList.add("activo");
}

//Asigno la función despliegaPestana al evento click de los enlaces de pestaña
document.querySelectorAll(".pestanas a").forEach(function(elemento) {
  elemento.addEventListener("click", despliegaPestana);
});
