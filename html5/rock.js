var rockShape = [[-20, 0], [-10, 8], [-8, 20], [6, 20], [14, 6], [18, 8], 
                 [20, -4], [2, -14], [0, -18], [-4, -12], [-12, -14]];
var rock = [], numRocks = 25;
function makeRocks(WIDTH, HEIGHT){
for(var i = 0; i < numRocks; ++i){
    var sx = Math.random() * WIDTH;
    var sy = Math.random() * HEIGHT;
    var ssx = Math.random() - 0.5;
    var ssy = Math.random() - 0.5;
    var sz = Math.random() * 15 + 1;
    var sa = Math.random() * Math.PI;
    for(var dy = 0; dy <= 2; ++dy){
      for(var dx = 0; dx <= 2; ++dx){
        rock[i + (dy * 3 + dx) * numRocks]
          = new Mobile(rockShape, sx + (dx - 1) * WIDTH, sy + (dy - 1) * HEIGHT, sz, sa, 0, -1, ssx, ssy);
      }
    }
  }
}