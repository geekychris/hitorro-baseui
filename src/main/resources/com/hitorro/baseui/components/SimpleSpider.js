/*
window.onload = function ()
{
   //drawCircles("canvas", 640, 480);
   var dataPoints = [
       {name : "alpha"},
       {name : "beta"},
       {name :"gamma"},
       {name :"omega"},
       {name :"sigma"}
   ];
   var valNames = [
       {name:"adam", color:"#00f", value:[1,5,6,7,2]},
       {name:"bob", color:"#0f0", value:[0,4,5,6,3]},
       {name:"carl", color:"#f00", value:[5,6,2,3,7]},
       {name:"david", color:"#f0f", value:[3,1,2,3,8]}
   ];
   var spec = {points: dataPoints, names: valNames, canvas: "canvas", width: 480, height: 480, radius:200, maxValue: 8};
   new BkSpider(spec);
};
  */
Tapestry.Initializer.simpleSpider = function (spec) {
    new HtSpider(spec);
}