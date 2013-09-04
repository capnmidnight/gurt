/// <reference path="mob.ts" />
var Gurt;
(function (Gurt) {
    var rockShape = [
        [-20, 0],
        [-10, 8],
        [-8, 20],
        [6, 20],
        [14, 6],
        [18, 8],
        [20, -4],
        [2, -14],
        [0, -18],
        [-4, -12],
        [-12, -14]
    ];
    Gurt.rock = [], Gurt.numRocks = 1000;
    function makeRocks() {
        for (var i = 0; i < Gurt.numRocks; ++i) {
            var sa = Math.random() * Math.PI;
            var sx = Math.cos(sa) * (Gurt.WIDTH + Gurt.HEIGHT);
            var sy = Math.sin(sa) * (Gurt.WIDTH + Gurt.HEIGHT);
            var ssx = (Math.random() - 0.5);
            var ssy = (Math.random() - 0.5);
            var sz = Math.random() * 15 + 1;
            Gurt.rock[i] = new Gurt.Mobile(rockShape, sx, sy, sz, sa, 0.0001, -1, ssx, ssy, 0, 0, 0, 0, 0);
        }
    }
    Gurt.makeRocks = makeRocks;
})(Gurt || (Gurt = {}));
//# sourceMappingURL=rock.js.map
