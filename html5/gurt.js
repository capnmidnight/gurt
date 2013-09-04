/// <reference path="config.ts" />
/// <reference path="mob.ts" />
/// <reference path="rock.ts" />
/// <reference path="stars.ts" />
/// <reference path="ship.ts" />
var Gurt;
(function (Gurt) {
    var canv1, canv2, front, back;
    var targetT = Math.floor(1000 / 60);
    var drag = 0.01;
    var anaglyph = true;
    var enemyX = Gurt.WIDTH / 2, enemyY = Gurt.HEIGHT / 2, dx, dy;
    var half_angle, anaglyph_x, anaglyph_y, i, j;

    function init() {
        canv1 = makeCanvas("front-buffer", Gurt.WIDTH, Gurt.HEIGHT);
        canv2 = makeCanvas("back-buffer", Gurt.WIDTH, Gurt.HEIGHT);
        document.body.appendChild(canv1);
        document.body.appendChild(canv2);
        front = canv1.getContext("2d");
        back = canv2.getContext("2d");
        back.globalCompositeOperation = "lighter";
        Gurt.makeRocks();
        lib.keyHandler.setup(document);
        lib.pump.start(update, draw, targetT);
    }
    Gurt.init = init;

    lib.keyHandler.add(192, 500, function (dt) {
        anaglyph = !anaglyph;
    });

    function update(dt) {
        Gurt.ship.update(dt);
        for (i = 0; i < Gurt.pingR.length; ++i) {
            if (Gurt.pingAge[i] > 0) {
                Gurt.pingR[i] += dt * Gurt.pingD[i];
                Gurt.pingAge[i] -= dt;
                if (Gurt.pingD[i] > 0) {
                    dx = Gurt.pingX[i] - enemyX;
                    dy = Gurt.pingY[i] - enemyY;
                    if (Math.abs(Gurt.pingR[i] - Math.sqrt(dx * dx + dy * dy)) <= 10) {
                        Gurt.pingD[i] = 0;
                    }
                }
            }
        }
        for (i = 0; i < Gurt.numBullets; ++i) {
            if (Gurt.bullet[i].ttl > 0) {
                Gurt.bullet[i].update(dt);
                dx = Gurt.bullet[i].x - enemyX;
                dy = Gurt.bullet[i].y - enemyY;
                if ((dx * dx + dy * dy) <= 100) {
                    alert("BOOM!");
                }
            }
        }
        for (i = 0; i < Gurt.rock.length; ++i) {
            Gurt.rock[i].update(dt);
        }
    }

    function draw() {
        back.clearRect(0, 0, Gurt.WIDTH, Gurt.HEIGHT);
        back.fillStyle = "#000000";
        back.fillRect(0, 0, Gurt.WIDTH, Gurt.HEIGHT);
        if (anaglyph) {
            eye(-1, "rgb(50, 0, 0)");
            eye(1, "rgb(0, 50, 50)");
        } else {
            eye(0, "rgb(255, 255, 255)");
        }
    }
    function eye(side, color) {
        half_angle = Gurt.ship.a * 0.5;
        anaglyph_x = side * Math.cos(half_angle);
        anaglyph_y = side * Math.sin(half_angle);
        back.lineWidth = 2;
        back.save();
        back.translate(Gurt.HALF_WIDTH, Gurt.HALF_HEIGHT);
        back.rotate(-half_angle);
        back.translate(-Gurt.ship.x, -Gurt.ship.y);
        back.strokeStyle = color;
        back.fillStyle = color;
        back.lineJoin = "miter";
        back.lineWidth = 4;
        Gurt.drawStars(back, Gurt.ship.x, Gurt.ship.y, anaglyph_x, anaglyph_y, Gurt.ship);
        for (i = 0; i < Gurt.numBullets; ++i) {
            if (Gurt.bullet[i].ttl > 0) {
                Gurt.bullet[i].draw(back, anaglyph_x, anaglyph_y, Gurt.ship);
            }
        }
        for (i = 0; i < Gurt.rock.length; ++i) {
            Gurt.rock[i].draw(back, anaglyph_x, anaglyph_y, Gurt.ship);
        }
        Gurt.ship.draw(back, anaglyph_x, anaglyph_y, Gurt.ship);
        for (i = 0; i < Gurt.numPings; ++i) {
            if (Gurt.pingAge[i] > 0) {
                j = Gurt.pingAge[i] % 256;
                back.beginPath();
                back.arc(Gurt.pingX[i] + anaglyph_x * Gurt.ship.z, Gurt.pingY[i] + anaglyph_y * Gurt.ship.z, Gurt.pingR[i], 0, 6.28319, false);
                back.fill();
            }
        }
        back.restore();
        front.drawImage(canv2, 0, 0);
    }
})(Gurt || (Gurt = {}));
//# sourceMappingURL=gurt.js.map
