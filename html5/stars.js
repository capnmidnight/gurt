/// <reference path="config.ts">
/// <reference path="mob.ts">
/// <reference path="gurt.ts">
var Gurt;
(function (Gurt) {
    Gurt.star = new Gurt.Mobile([[0, -4], [1, -1], [4, 0], [1, 1], [0, 4], [-1, 1], [-4, 0], [-1, -1], [0, -4]], 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0);
    function drawStars(g, x, y, anaglyph_x, anaglyph_y, ship) {
        var sx = Math.floor(x / Gurt.WIDTH) - 1;
        var sy = Math.floor(y / Gurt.HEIGHT) - 1;
        var r = new RANDO();
        for (var dy = 0; dy < 3; ++dy, ++sy, sx -= 3) {
            for (var dx = 0; dx < 3; ++dx, ++sx) {
                r.reseed(sx, sy);
                var n = Math.floor(r.next() * 10) + 10;
                for (var i = 0; i < n; ++i) {
                    var nx = Math.floor(r.next() * Gurt.WIDTH) + sx * Gurt.WIDTH;
                    var ny = Math.floor(r.next() * Gurt.HEIGHT) + sy * Gurt.HEIGHT;
                    var nz = Math.floor(r.next() * Gurt.DEPTH);
                    Gurt.star.init(nx, ny, nz, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0);
                    Gurt.star.draw(g, anaglyph_x, anaglyph_y, ship);
                }
            }
        }
    }
    Gurt.drawStars = drawStars;
})(Gurt || (Gurt = {}));
//# sourceMappingURL=stars.js.map
