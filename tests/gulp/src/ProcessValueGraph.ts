import Plotly = require("plotly.js");
import ProcessValue from "./ProcessValue";
import {DataPoint} from "./DataPoint";
import {Datum} from "plotly.js";

export default class ProcessValueGraph extends HTMLElement {

  static get observedAttributes() {
    return ['disabled'];
  }

  // assumed time span is one minute
  readonly arrayLength: number = 60 * 1000 / 100; // 60 seconds * 1000 milliseconds / 100 milliseconds

  // string index signature to allow for some extra properties
  [propName: string]: any; 

  private processValues: ProcessValue[] = []; // TODO use a Map

  constructor() {
    // If you define a constructor, always call super() first!
    super();

    // Use a template to ensure that content is only parsed once
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

    let shadowRoot = this.attachShadow({mode: 'open'});
    shadowRoot.appendChild(template.content.cloneNode(true));
  }

  // A getter/setter for a disabled property.
  get disabled(): boolean {
    return this.hasAttribute('disabled');
  }

  set disabled(val: boolean) {
    // Reflect the value of the disabled property as an HTML attribute.
    if (val) {
      this.setAttribute('disabled', '');
    }
    else {
      this.removeAttribute('disabled');
    }
    this.onDisabledChanged();
  }

  private onDisabledChanged(): void {
    // TODO
  }

  public addProcessValue(processValue: ProcessValue): boolean {
    for (let i = 0; i < this.processValues.length; i++) {
      if (this.processValues[i].id === processValue.id) {
        console.warn('a process value with id <' + processValue.id + '> already exists');
        return false;
      }
    }
    this.processValues.push(processValue);
    let index: number = this.processValues.length - 1;

    let plotlyRoot = this.getPlotlyRoot();

    let trace: Plotly.Data = {};
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

    let layoutUpdate: Partial<Plotly.Layout> = {};

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
      yAxis.titlefont ? (yAxis.titlefont.color = processValue.color) : (yAxis.titlefont = {color: processValue.color});
      yAxis.tickfont ? (yAxis.tickfont.color = processValue.color) : (yAxis.tickfont = {color: processValue.color});
      yAxis.side = (index % 2 == 0) ? 'left' : 'right';
      yAxis.range = [processValue.minValue, processValue.maxValue];
    }

    Plotly.relayout(plotlyRoot, layoutUpdate);
    
    return true;
  }

  public addProcessValues(processValues: ProcessValue[]): void {
    processValues.forEach(value => this.addProcessValue(value));
  }

  public removeProcessValueWithId(processValueId: string): boolean {
    for (let i = 0; i < this.processValues.length; i++) {
      if (this.processValues[i].id === processValueId) {
        this.processValues.splice(i, 1);
        // TODO
        return true;
      }
    }
    console.warn('failed to remove a process value with id <' + processValueId + '>');
    return false;
  }

  public removeProcessValue(processValue: ProcessValue): boolean {
    return this.removeProcessValueWithId(processValue.id);
  }

  public removeProcessValues(processValues: ProcessValue[]): void {
    processValues.forEach(value => this.removeProcessValue(value));
  }

  public updateProcessValue(processValue: ProcessValue): boolean {
    for (let i = 0; i < this.processValues.length; i++) {
      if (this.processValues[i].id === processValue.id) {
        this.processValues[i] = processValue; // TODO replace the whole object or only properties of it?
        // TODO
        return true;
      }
    }
    console.warn('failed to update process value with id <' + processValue.id + '>');
    return false;
  }

  private getProcessValueIndexForId(processValueId: string) : number | null {
    for (let i = 0; i < this.processValues.length; i++) {
      if (this.processValues[i].id === processValueId) {
        return i;
      }
    }
    return null;
  }
  
  public addDataPoint(dataPoint: DataPoint): boolean {

    let traceUpdate: Plotly.Data = {};
    traceUpdate.x = [[dataPoint.timestamp]];
    traceUpdate.y = [];

    dataPoint.values.forEach(val => {
      let index = this.getProcessValueIndexForId(val.processValueId);
      if (index == null) {
        console.warn('received value for unknown process value with id <' + val.processValueId + '>');
        return;
      }
      (traceUpdate.y as Datum[][]).push([val.value]);
    });

    Plotly.update(this.getPlotlyRoot(), traceUpdate, {});
    return true;
  }

  private static getXAxisTimeRange(): Array<Date> {
    let now = new Date().getTime();
    return [new Date(now - 60000), new Date(now)]; // 60 seconds
  }

  private getPlotlyRoot(): HTMLElement {
    if (this.shadowRoot === null) {
      throw new Error('failed to attach shadow dom');
    }
    return this.shadowRoot.getElementById('myChart')!;
  }
  
  // custom element reactions /////////////////////////////////////////////////
  //
  connectedCallback() {
    // Called every time the element is inserted into the DOM. 
    // Useful for running setup code, such as fetching resources or rendering. 
    // Generally, you should try to delay work until this time.

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
    // Called every time the element is removed from the DOM. 
    // Useful for running clean up code. 
    
    if (this.chart) {
      this.chart.destroy();
      delete this.chart;
      this.chart = undefined;
    }
  }

  attributeChangedCallback(attrName: string, oldVal: any, newVal: any) {
    // Called when an observed attribute has been added, removed, updated, or replaced. 
    // Also called for initial values when an element is created by the parser, or upgraded. 
    // Note: only attributes listed in the observedAttributes property will receive this callback.

    if (attrName === 'disabled') {
      this.onDisabledChanged();
      return;
    }
  }

  //
  // custom element reactions /////////////////////////////////////////////////

  _upgradeProperty(prop: string) {
    // A developer might attempt to set a property on the custom element before its definition has been loaded. 
    // This is especially true if the developer is using a framework which handles loading components, 
    // inserting them into to the page, and binding their properties to a model.
    // -> ensure that the developer's property does not shadow the custom element's own property
    if (this.hasOwnProperty(prop)) {
      let value = this[prop]; // save previous value
      delete this[prop];
      this[prop] = value; // restore previous value
    }
  }

}

const supportsCustomElementsV1: boolean = 'customElements' in window;
if (supportsCustomElementsV1) {
  window.customElements.define('process-value-graph', ProcessValueGraph);
}
else {
  console.error('fatal error: custom elements are not supported by this browser');
}
