function Mobile(poly, x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass){
  this.poly = poly;
  this.init(x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass);
}

Mobile.prototype.init = function(x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass){
  this.x = x || 0;
  this.y = y || 0;
  this.z = z || 0;
  this.scale = 1 - this.z/64;
  this.a = a || 0;
  this.drag = drag || 0;
  if(ttl === null || ttl === undefined)
    this.ttl = -1;
  else
    this.ttl = ttl;
  this.dx = dx || 0;
  this.dy = dy || 0;
  this.da = da || 0;
  this.ddx = ddx || 0;
  this.ddy = ddy || 0;
  this.dda = dda || 0;
  this.mass = mass || 0;
};

Mobile.prototype.update = function(dt){
  this.dx += (this.ddx - this.drag * this.dx) * dt;
  this.dy += (this.ddy - this.drag * this.dy) * dt;
  this.da += (this.dda - this.drag * this.da) * dt;
  this.x += this.dx * dt;
  this.y += this.dy * dt;
  this.a += this.da * dt;
  this.ddx = 0;
  this.ddy = 0;
  this.dda = 0;
  
  if(this.ttl > 0)
  {
    this.ttl -= dt;
    // don't let the ttl get negative, because negative
    // indicates that we don't care about the ttl value.
    if(this.ttl < 0)
      this.ttl = 0;
  }
};
var px, py, hx, hy, d, pa;
Mobile.prototype.draw = function(g2d, anaglyph_x, analgyph_y){
  g2d.save();
  px = (this.x - ship.x) / this.z;
  py = (this.y - ship.y) / this.z;
  g2d.translate(
    this.x + px + anaglyph_x * this.z, 
    this.y + py + anaglyph_y * this.z);
  g2d.rotate(this.a);
    back.scale(this.scale, this.scale);
  g2d.beginPath();
  g2d.moveTo(this.poly[0][0], this.poly[0][1]);
  for(var i = 1; i < this.poly.length; ++i)
    g2d.lineTo(this.poly[i % this.poly.length][0], this.poly[i % this.poly.length][1]);
  g2d.closePath();
  g2d.stroke();
  g2d.restore();
};