function makeCanvas(id: string, width: number, height: number) {
    var canv = document.createElement("canvas");
    canv.id = id;
    canv.width = width;
    canv.height = height;
    canv.style.width = width + "px";
    canv.style.height = height + "px";
    return canv;
}

class RANDO {
    A: number;
    M: number;
    Q: number;
    R: number;
    oneOverM: number;
    seed: number;
    hi: number;
    lo: number;
    test: number;

    constructor() {
        this.A = 48271;
        this.M = 2147483647;
        this.Q = this.M / this.A;
        this.R = this.M % this.A;
        this.oneOverM = 1.0 / this.M;
    }

    next() {
        this.hi = this.seed / this.Q;
        this.lo = this.seed % this.Q;
        this.test = this.A * this.lo - this.R * this.hi;
        if (this.test > 0) {
            this.seed = this.test;
        } else {
            this.seed = this.test + this.M;
        }
        return (this.seed * this.oneOverM);
    }

    reseed(x, y) {
        this.seed = 2345678901 + x * 0xFFFFFF + y * 0xFFFF;
    }
}

var lib = {
    keyHandler: {
        keys: [],
        keycool: [],
        keyfuncs: [],
        keydown: function (evt) {
            if (!evt) {
                evt = event;
            }
            lib.keyHandler.keys[evt.keyCode] = true;
            console.log(evt.keyCode);
        },
        keyup: function (evt) {
            if (!evt) {
                evt = event;
            }
            lib.keyHandler.keys[evt.keyCode] = false;
        },
        setup: function (obj) {
            obj.addEventListener("keydown", lib.keyHandler.keydown, false);
            obj.addEventListener("keyup", lib.keyHandler.keyup, false);
        },
        add: function (code: number, cooldown: number, f) {
            lib.keyHandler.keyfuncs[lib.keyHandler.keyfuncs.length] = function (dt) {
                if (lib.keyHandler.keys[code]
                        && (!lib.keyHandler.keycool[code]
                        || lib.keyHandler.keycool[code] <= 0)) {
                    f(dt);
                    lib.keyHandler.keycool[code] = cooldown;
                } else if (lib.keyHandler.keycool[code] > 0) {
                    lib.keyHandler.keycool[code] -= dt;
                }
            };
        }
    },
    pump: {
        lastTime: 0,
        curTime: 0,
        dTime: 0,
        accumTime: 0,
        timer: null,
        i: 0,
        start: function (updateFunc, drawFunc, targetTime) {
            lib.pump.lastTime = Date.now();
            var tick = function () {
                lib.pump.curTime = Date.now();
                lib.pump.dTime = lib.pump.curTime - lib.pump.lastTime;
                for (lib.pump.accumTime += lib.pump.dTime;
                     lib.pump.accumTime > targetTime;
                     lib.pump.accumTime -= targetTime) {
                    for (lib.pump.i = 0;
                         lib.pump.i < lib.keyHandler.keyfuncs.length;
                         lib.pump.i++) {
                        lib.keyHandler.keyfuncs[lib.pump.i](targetTime);
                    }
                    updateFunc(targetTime);
                }
                drawFunc();
                lib.pump.lastTime = lib.pump.curTime;
                lib.pump.timer = setTimeout(tick, Math.max(targetTime - lib.pump.dTime, 1));
            };
            lib.pump.timer = setTimeout(tick, 1);
        }
    }
};