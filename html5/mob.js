var Gurt;
(function (Gurt) {
    "use strict";

    var Mobile = (function () {
        function Mobile(poly, x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass) {
            this.poly = poly;
            this.init(x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass);
        }
        Mobile.prototype.init = function (x, y, z, a, drag, ttl, dx, dy, da, ddx, ddy, dda, mass) {
            this.x = x || 0;
            this.y = y || 0;
            this.z = z || 0;
            this.scale = 1 - this.z / 64;
            this.a = a || 0;
            this.drag = drag || 0;
            if (ttl === null || ttl === undefined)
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

        Mobile.prototype.update = function (dt) {
            this.dx += (this.ddx - this.drag * this.dx) * dt;
            this.dy += (this.ddy - this.drag * this.dy) * dt;
            this.da += (this.dda - this.drag * this.da) * dt;
            this.x += this.dx * dt;
            this.y += this.dy * dt;
            this.a += this.da * dt;
            this.ddx = 0;
            this.ddy = 0;
            this.dda = 0;

            if (this.ttl > 0) {
                this.ttl -= dt;

                if (this.ttl < 0)
                    this.ttl = 0;
            }
        };

        Mobile.prototype.draw = function (g2d, anaglyph_x, anaglyph_y, ship) {
            g2d.save();
            this.px = (this.x - ship.x) / Math.sqrt(this.z);
            this.py = (this.y - ship.y) / Math.sqrt(this.z);
            g2d.translate(this.x + this.px + anaglyph_x * this.z, this.y + this.py + anaglyph_y * this.z);
            g2d.rotate(this.a);
            g2d.scale(this.scale, this.scale);
            g2d.beginPath();
            g2d.moveTo(this.poly[0][0], this.poly[0][1]);
            for (var i = 1; i < this.poly.length; ++i)
                g2d.lineTo(this.poly[i % this.poly.length][0], this.poly[i % this.poly.length][1]);
            g2d.closePath();
            g2d.stroke();
            g2d.restore();
        };
        return Mobile;
    })();
    Gurt.Mobile = Mobile;
})(Gurt || (Gurt = {}));
//# sourceMappingURL=mob.js.map
