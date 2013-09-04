var star = new Mobile([[0, -4], [1, -1], [4, 0], [1, 1], [0, 4], [-1, 1], [-4, 0], [-1, -1], [0, -4]]);

function RANDO(){
}

RANDO.prototype.A = 48271;
RANDO.prototype.M = 2147483647;
RANDO.prototype.Q = RANDO.prototype.M / RANDO.prototype.A;
RANDO.prototype.R = RANDO.prototype.M % RANDO.prototype.A;
RANDO.prototype.oneOverM = 1.0 / RANDO.prototype.M;
RANDO.prototype.next = function(){
  this.hi = this.seed / this.Q;
  this.lo = this.seed % this.Q;
  this.test = this.A * this.lo - this.R * this.hi;
  if(this.test > 0){
    this.seed = this.test;
  } else {
    this.seed = this.test + this.M;
  }
  return (this.seed * this.oneOverM);
}

RANDO.prototype.reseed = function(x, y){
  this.seed = 2345678901 + x * 0xFFFFFF + y * 0xFFFF;
};

function drawStars(g, x, y, anaglyph_x, anaglyph_y){
  var sx = Math.floor(x / WIDTH) - 1;
  var sy = Math.floor(y / HEIGHT) - 1;
  var r = new RANDO();
  for(var dy = 0; dy < 3; ++dy, ++sy, sx -= 3){
    for(var dx = 0; dx < 3; ++dx, ++sx){
      r.reseed(sx, sy);
      var n = Math.floor(r.next() * 10) + 10;
      for(var i = 0; i < n; ++i){
        var nx = Math.floor(r.next() * WIDTH) + sx * WIDTH;
        var ny = Math.floor(r.next() * HEIGHT) + sy * HEIGHT;
        var nz = Math.floor(r.next() * DEPTH);
        star.init(nx, ny, nz);
        star.draw(g, anaglyph_x, anaglyph_y);
      }
    }
  }
}