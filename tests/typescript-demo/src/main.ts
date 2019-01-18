import {sayHello} from "./greet";

function showHello(divName: string, name: string) {
  const elt = document.getElementById(divName);
  if (elt == null) {
    throw new Error(`Uups. no element found for ${divName}`)
  }
  elt.innerText = sayHello(name);
}


showHello("greeting", "TypeScript");
