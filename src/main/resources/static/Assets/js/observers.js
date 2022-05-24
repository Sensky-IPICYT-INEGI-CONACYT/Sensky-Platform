/**
* This file contains the necessary code for the effects that will be 
* shown on the screen by scroll. Dependent effects of the "Intersection Observer" API.
* @header will contain the header element of the DOM.
* @sectionOne is the section in which I want the observer to be placed.
* @sectionOneOptions will help to stablish some rules, for example, in this 
   case I want the observer to be triggered -70% before it leaves section one.
* @IntersectionObserver is an interface that provides methods for creating and managing an observer.
* Everytime that my entry (sectionOne in the first case) is not intersecting with the observer
  I will add some atributes to the nav-bar in order to change it's appearence by DOM Manipulation.
  When my entry is intersecting with my observer then I'll remove those attributes
**/

const header = document.querySelector("#nav");
const sectionOne = document.querySelector(".redes");
const faders = document.querySelectorAll(".fade-in");

const sectionOneOptions = {
	rootMargin: "-20% 0% 0% 0%"
};

const sectionOneObserver = new IntersectionObserver(function(
	entries,
	sectionOneObserver
) {
	entries.forEach(entry => {
		if(!entry.isIntersecting) {
			header.classList.add("nav-changed");
			console.log(entry);
		} else {
			header.classList.remove("nav-changed");
		}
	});
},
sectionOneOptions);

sectionOneObserver.observe(sectionOne);


const appearOptions = {
	treshold: 1,  //Unless the observer is totally intersecting with my entry
	rootMargin: "0px 0px -600px 0px"
};

const appearOnScroll = new IntersectionObserver(function(
	entries, 
	appearOnScroll
) {
	entries.forEach(entry => {
		if(!entry.isIntersecting) {
			return;
		} else {
			entry.target.classList.add('appear');
			appearOnScroll.unobserve(entry.target);
		}
	});
}, 
appearOptions);

faders.forEach(fader => {
	appearOnScroll.observe(fader);
})
