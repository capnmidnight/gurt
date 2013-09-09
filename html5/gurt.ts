/// <reference path="config.ts" />
/// <reference path="mob.ts" />
/// <reference path="rock.ts" />
/// <reference path="stars.ts" />
/// <reference path="ship.ts" />

module Gurt{
    var canv1: HTMLCanvasElement;
    var canv2: HTMLCanvasElement;
    var front: CanvasRenderingContext2D;
    var back: CanvasRenderingContext2D;
    var targetT: number = Math.floor(1000 / 60);
    var drag: number = 0.01;
    var anaglyph: boolean = true;
    var enemyX: number = Gurt.WIDTH / 2;
    var enemyY:number = Gurt.HEIGHT / 2;
    var dx:number, dy:number;
    var half_angle:number, anaglyph_x:number, anaglyph_y:number, i:number, j:number;

    export function init() {
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

    lib.keyHandler.add(192, 500, function (dt) {
        anaglyph = !anaglyph;
    });

    function update(dt) {
        Gurt.ship.update(dt);
        for (i = 0; i < pingR.length; ++i) {
            if (pingAge[i] > 0) {
                pingR[i] += dt * pingD[i];
                pingAge[i] -= dt;
                if (pingD[i] > 0) {
                    dx = pingX[i] - enemyX;
                    dy = pingY[i] - enemyY;
                    if (Math.abs(pingR[i] - Math.sqrt(dx * dx + dy * dy)) <= 10) {
                        pingD[i] = 0;
                    }
                }
            }
        }
        for (i = 0; i < numBullets; ++i) {
            if (bullet[i].ttl > 0) {
                bullet[i].update(dt);
                dx = bullet[i].x - enemyX;
                dy = bullet[i].y - enemyY;
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
        back.clearRect(0, 0, WIDTH, HEIGHT);
        back.fillStyle = "#000000";
        back.fillRect(0, 0, WIDTH, HEIGHT);
        if (anaglyph) {
            eye(-1, "rgb(50, 0, 0)");
            eye(1, "rgb(0, 50, 50)");
        }
        else {
            eye(0, "rgb(255, 255, 255)");
        }
    }
    function eye(side, color) {
        half_angle = ship.a * 0.5;
        anaglyph_x = side * Math.cos(half_angle);
        anaglyph_y = side * Math.sin(half_angle);
        back.lineWidth = 2;
        back.save();
        back.translate(HALF_WIDTH, HALF_HEIGHT);
        back.rotate(-half_angle);
        back.translate(-ship.x, -ship.y);
        back.strokeStyle = color;
        back.fillStyle = color;
        back.lineJoin = "miter";
        back.lineWidth = 4;
        drawStars(back, ship.x, ship.y, anaglyph_x, anaglyph_y, ship);
        for (i = 0; i < numBullets; ++i) {
            if (bullet[i].ttl > 0) {
                bullet[i].draw(back, anaglyph_x, anaglyph_y, ship);
            }
        }
        for (i = 0; i < rock.length; ++i) {
            rock[i].draw(back, anaglyph_x, anaglyph_y, ship);
        }
        ship.draw(back, anaglyph_x, anaglyph_y, ship);
        for (i = 0; i < numPings; ++i) {
            if (pingAge[i] > 0) {
                j = pingAge[i] % 256;
                back.beginPath();
                back.arc(pingX[i] + anaglyph_x * ship.z, pingY[i] + anaglyph_y * ship.z, pingR[i], 0, 6.28319, false);
                back.fill();
            }
        }
        back.restore();
        front.drawImage(canv2, 0, 0);
    }
}