/// <reference path="mob.ts" />
var Gurt;
(function (Gurt) {
    Gurt.ship = new Gurt.Mobile([[-10, -7], [10, 0], [-10, 7], [-5, 0], [-10, -7]], 0, 0, 10, 0, 0.01, -1, 0, 0, 0, 0, 0, 0, 0);

    Gurt.bullet = [], Gurt.numBullets = 3, Gurt.curBullet = 0, Gurt.MAX_BULLET_AGE = 2000, Gurt.BULLET_SPEED = 0.05;
    Gurt.pingX = [], Gurt.pingY = [], Gurt.pingAge = [], Gurt.pingR = [], Gurt.pingD = [], Gurt.numPings = 3, Gurt.curPing = 0, Gurt.MAX_PING_AGE = 10000;

    for (var i = 0; i < Gurt.numBullets; ++i) {
        Gurt.bullet[i] = new Gurt.Mobile([[-1, 0], [0, 1], [1, 0], [0, -1]], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }
    Gurt.da = Math.PI / 50000, Gurt.spd = 0.006;
    for (var i = 0; i < Gurt.numPings; ++i) {
        Gurt.pingX[i] = Gurt.pingY[i] = Gurt.pingAge[i] = Gurt.pingR[i] = Gurt.pingD[i] = 0;
    }

    lib.keyHandler.add(65, 0, function () {
        Gurt.ship.dda = -Gurt.da;
    });

    lib.keyHandler.add(68, 0, function () {
        Gurt.ship.dda = Gurt.da;
    });

    lib.keyHandler.add(87, 0, function () {
        Gurt.ship.ddx = Math.cos(Gurt.ship.a) * Gurt.spd;
        Gurt.ship.ddy = Math.sin(Gurt.ship.a) * Gurt.spd;
    });

    lib.keyHandler.add(83, 0, function () {
        Gurt.ship.ddx = -Math.cos(Gurt.ship.a) * Gurt.spd;
        Gurt.ship.ddy = -Math.sin(Gurt.ship.a) * Gurt.spd;
    });

    lib.keyHandler.add(32, 250, function () {
        if (Gurt.bullet[Gurt.curBullet].ttl <= 0) {
            Gurt.bullet[Gurt.curBullet].init(Gurt.ship.x, Gurt.ship.y, 10, Gurt.ship.a, 0, Gurt.MAX_BULLET_AGE, 0, 0, 0, Gurt.BULLET_SPEED * Math.cos(Gurt.ship.a), Gurt.BULLET_SPEED * Math.sin(Gurt.ship.a), 1);
            Gurt.curBullet = (Gurt.curBullet + 1) % Gurt.numBullets;
        }
    });

    lib.keyHandler.add(69, 500, function () {
        if (Gurt.pingAge[Gurt.curPing] <= 0) {
            Gurt.pingX[Gurt.curPing] = Gurt.ship.x;
            Gurt.pingY[Gurt.curPing] = Gurt.ship.y;
            Gurt.pingAge[Gurt.curPing] = Gurt.MAX_PING_AGE;
            Gurt.pingR[Gurt.curPing] = 0;
            Gurt.pingD[Gurt.curPing] = 0.05;
            Gurt.curPing = (Gurt.curPing + 1) % Gurt.numPings;
        }
    });
})(Gurt || (Gurt = {}));
//# sourceMappingURL=ship.js.map
