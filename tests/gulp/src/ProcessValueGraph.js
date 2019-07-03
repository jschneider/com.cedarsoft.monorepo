"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const Plotly = require("plotly.js");
class ProcessValueGraph extends HTMLElement {
    constructor() {
        super();
        this.arrayLength = 60 * 1000 / 100;
        this.processValues = [];
        let template = document.createElement('template');
        template.innerHTML = `
  <style>:host { 
      display: block; 
      contain: content; 
    }
    :host([hidden]) {
      display: none;
    }
  </style>
  <div id="myChart" style="width:100%;height:100%;"></div>
  <slot></slot>
`;
        let shadowRoot = this.attachShadow({ mode: 'open' });
        shadowRoot.appendChild(template.content.cloneNode(true));
    }
    static get observedAttributes() {
        return ['disabled'];
    }
    get disabled() {
        return this.hasAttribute('disabled');
    }
    set disabled(val) {
        if (val) {
            this.setAttribute('disabled', '');
        }
        else {
            this.removeAttribute('disabled');
        }
        this.onDisabledChanged();
    }
    onDisabledChanged() {
    }
    addProcessValue(processValue) {
        for (let i = 0; i < this.processValues.length; i++) {
            if (this.processValues[i].id === processValue.id) {
                console.warn('a process value with id <' + processValue.id + '> already exists');
                return false;
            }
        }
        this.processValues.push(processValue);
        let index = this.processValues.length - 1;
        let plotlyRoot = this.getPlotlyRoot();
        let trace = {};
        if (index > 0) {
            trace.yaxis = 'y' + this.processValues.length;
        }
        trace.connectgaps = true;
        trace.type = 'scattergl';
        trace.mode = 'lines';
        trace.name = processValue.label;
        if (trace.marker === undefined) {
            trace.marker = {};
        }
        trace.marker.color = processValue.color;
        trace.marker.size = 8;
        if (trace.line === undefined) {
            trace.line = {};
        }
        trace.line.color = processValue.color;
        trace.line.width = 3;
        trace.line.shape = 'linear';
        trace.line.dash = 'solid';
        Plotly.addTraces(plotlyRoot, trace);
        let layoutUpdate = {};
        let yAxis;
        switch (index) {
            case 0:
                yAxis = layoutUpdate.yaxis;
                break;
            case 1:
                yAxis = layoutUpdate.yaxis2;
                break;
            case 2:
                yAxis = layoutUpdate.yaxis3;
                break;
            case 3:
                yAxis = layoutUpdate.yaxis4;
                break;
            case 4:
                yAxis = layoutUpdate.yaxis5;
                break;
            case 5:
                yAxis = layoutUpdate.yaxis6;
                break;
            case 6:
                yAxis = layoutUpdate.yaxis7;
                break;
            case 7:
                yAxis = layoutUpdate.yaxis8;
                break;
            case 8:
                yAxis = layoutUpdate.yaxis9;
                break;
            default:
                console.warn('plotly does not support more than 8 y-axis at once; there will be no y-axis for process value with id <' + processValue.id + '>');
                break;
        }
        if (yAxis) {
            yAxis.title = processValue.label;
            yAxis.titlefont ? (yAxis.titlefont.color = processValue.color) : (yAxis.titlefont = { color: processValue.color });
            yAxis.tickfont ? (yAxis.tickfont.color = processValue.color) : (yAxis.tickfont = { color: processValue.color });
            yAxis.side = (index % 2 == 0) ? 'left' : 'right';
            yAxis.range = [processValue.minValue, processValue.maxValue];
        }
        Plotly.relayout(plotlyRoot, layoutUpdate);
        return true;
    }
    addProcessValues(processValues) {
        processValues.forEach(value => this.addProcessValue(value));
    }
    removeProcessValueWithId(processValueId) {
        for (let i = 0; i < this.processValues.length; i++) {
            if (this.processValues[i].id === processValueId) {
                this.processValues.splice(i, 1);
                return true;
            }
        }
        console.warn('failed to remove a process value with id <' + processValueId + '>');
        return false;
    }
    removeProcessValue(processValue) {
        return this.removeProcessValueWithId(processValue.id);
    }
    removeProcessValues(processValues) {
        processValues.forEach(value => this.removeProcessValue(value));
    }
    updateProcessValue(processValue) {
        for (let i = 0; i < this.processValues.length; i++) {
            if (this.processValues[i].id === processValue.id) {
                this.processValues[i] = processValue;
                return true;
            }
        }
        console.warn('failed to update process value with id <' + processValue.id + '>');
        return false;
    }
    getProcessValueIndexForId(processValueId) {
        for (let i = 0; i < this.processValues.length; i++) {
            if (this.processValues[i].id === processValueId) {
                return i;
            }
        }
        return null;
    }
    addDataPoint(dataPoint) {
        let traceUpdate = {};
        traceUpdate.x = [[dataPoint.timestamp]];
        traceUpdate.y = [];
        dataPoint.values.forEach(val => {
            let index = this.getProcessValueIndexForId(val.processValueId);
            if (index == null) {
                console.warn('received value for unknown process value with id <' + val.processValueId + '>');
                return;
            }
            traceUpdate.y.push([val.value]);
        });
        Plotly.update(this.getPlotlyRoot(), traceUpdate, {});
        return true;
    }
    static getXAxisTimeRange() {
        let now = new Date().getTime();
        return [new Date(now - 60000), new Date(now)];
    }
    getPlotlyRoot() {
        if (this.shadowRoot === null) {
            throw new Error('failed to attach shadow dom');
        }
        return this.shadowRoot.getElementById('myChart');
    }
    connectedCallback() {
        Plotly.newPlot(this.getPlotlyRoot(), [], {
            xaxis: {
                title: '',
                type: 'date',
                showgrid: true,
                zeroline: false,
                range: ProcessValueGraph.getXAxisTimeRange(),
                autorange: true,
                showline: true,
                showticklabels: true,
                linecolor: 'rgb(204,204,204)',
                linewidth: 2,
                ticks: 'outside',
                tickcolor: 'rgb(204,204,204)',
                tickwidth: 2,
                ticklen: 5,
                tickfont: {
                    family: 'Arial',
                    size: 12,
                    color: 'rgb(82, 82, 82)'
                }
            }
        }).then(value => {
            console.log('successfully initialized plotly plot');
        }).catch(reason => {
            console.error('failed to initialize plotly plot: ' + reason);
        });
        this._upgradeProperty('disabled');
    }
    disconnectedCallback() {
        if (this.chart) {
            this.chart.destroy();
            delete this.chart;
            this.chart = undefined;
        }
    }
    attributeChangedCallback(attrName, oldVal, newVal) {
        if (attrName === 'disabled') {
            this.onDisabledChanged();
            return;
        }
    }
    _upgradeProperty(prop) {
        if (this.hasOwnProperty(prop)) {
            let value = this[prop];
            delete this[prop];
            this[prop] = value;
        }
    }
}
exports.default = ProcessValueGraph;
const supportsCustomElementsV1 = 'customElements' in window;
if (supportsCustomElementsV1) {
    window.customElements.define('process-value-graph', ProcessValueGraph);
}
else {
    console.error('fatal error: custom elements are not supported by this browser');
}
//# sourceMappingURL=ProcessValueGraph.js.map