var star = new Mobile([[0, -4], [1, -1], [4, 0], [1, 1], [0, 4], [-1, 1], [-4, 0], [-1, -1], [0, -4]]);
var STAR_R = Math.sqrt(WIDTH*WIDTH + HEIGHT*HEIGHT);
function RANDO(x, y){
  this.seed = 2345678901 + x * 0xFFFFFF + y * 0xFFFF;
}

RANDO.prototype.A = 48271;
RANDO.prototype.M = 2147483647;
RANDO.prototype.Q = RANDO.prototype.M / RANDO.prototype.A;
RANDO.prototype.R = RANDO.prototype.M % RANDO.prototype.A;
RANDO.prototype.oneOverM = 1.0 / RANDO.prototype.M;
RANDO.prototype.next = function(){
  var hi = this.seed / this.Q;
  var lo = this.seed % this.Q;
  var test = this.A * lo - this.R * hi;
  if(test > 0){
    this.seed = test;
  } else {
    this.seed = test + this.M;
  }
  return (this.seed * this.oneOverM);
}

function drawStars(g, x, y, color, anaglyph_x, anaglyph_y){
  var sx = Math.floor(x / WIDTH) - 1;
  var sy = Math.floor(y / HEIGHT) - 1;
  for(var dy = 0; dy < 3; ++dy, ++sy, sx -= 3){
    for(var dx = 0; dx < 3; ++dx, ++sx){
      var r = new RANDO(sx, sy);
      var n = Math.floor(r.next() * 10) + 10;
      for(var i = 0; i < n; ++i){
        var nx = Math.floor(r.next() * WIDTH) + sx * WIDTH;
        var ny = Math.floor(r.next() * HEIGHT) + sy * HEIGHT;
        var nz = Math.floor(r.next() * DEPTH);
        star.moveTo(nx, ny, nz);
        g.strokeStyle = color + (20 - nz) / 30 + ")";
        star.draw(g, anaglyph_x, anaglyph_y);
      }
    }
  }
}