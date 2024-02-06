function setLayerDisplay(layerName) {
  if (document.getElementById) {

    var w = document.getElementById(layerName);
    if (w.style.display == "none") {
      w.style.display = "block";
    } else {
      w.style.display = "none";
    }
  }
}
function new_window(url) {
  link = window.open(
      url, "Link",
      "toolbar=0,location=0,directories=0,status=0,menubar=no,scrollbars=yes,resizable=yes,width=522,height=282");
}
