"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class ProcessValue {
    constructor(params = {}) {
        let { id = '', label = '', unit = '', visible = true, selected = false, color = '#000000', minValue = 0.0, maxValue = 100.0 } = params;
        if (id.length < 1) {
            throw new Error('id must not be empty');
        }
        this.id = id;
        this.label = label;
        this.unit = unit;
        this.visible = visible;
        this.selected = selected;
        this.color = color;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
exports.default = ProcessValue;
//# sourceMappingURL=ProcessValue.js.map