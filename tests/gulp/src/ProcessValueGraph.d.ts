import ProcessValue from "./ProcessValue";
import { DataPoint } from "./DataPoint";
export default class ProcessValueGraph extends HTMLElement {
    static readonly observedAttributes: string[];
    readonly arrayLength: number;
    [propName: string]: any;
    private processValues;
    constructor();
    disabled: boolean;
    private onDisabledChanged;
    addProcessValue(processValue: ProcessValue): boolean;
    addProcessValues(processValues: ProcessValue[]): void;
    removeProcessValueWithId(processValueId: string): boolean;
    removeProcessValue(processValue: ProcessValue): boolean;
    removeProcessValues(processValues: ProcessValue[]): void;
    updateProcessValue(processValue: ProcessValue): boolean;
    private getProcessValueIndexForId;
    addDataPoint(dataPoint: DataPoint): boolean;
    private static getXAxisTimeRange;
    private getPlotlyRoot;
    connectedCallback(): void;
    disconnectedCallback(): void;
    attributeChangedCallback(attrName: string, oldVal: any, newVal: any): void;
    _upgradeProperty(prop: string): void;
}
