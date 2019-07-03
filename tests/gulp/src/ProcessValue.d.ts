export default class ProcessValue {
    id: string;
    label: string;
    unit: string;
    visible: boolean;
    selected: boolean;
    color: string;
    minValue: number;
    maxValue: number;
    constructor(params?: ProcessValue);
}
