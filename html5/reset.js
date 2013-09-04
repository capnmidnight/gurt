if (document && !document.addEventListener) {
    Object.prototype.addEventListener = function (e, f, t) {
        this.attachEvent("on" + e, f);
    };
}