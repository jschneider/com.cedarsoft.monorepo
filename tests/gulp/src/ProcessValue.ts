export default class ProcessValue {
  public id: string;
  public label: string;
  public unit: string;
  public visible: boolean;
  public selected: boolean;
  public color: string;
  public minValue: number;
  public maxValue: number;

  constructor(params: ProcessValue = {} as ProcessValue) {
    let {
      id = '',
      label = '',
      unit = '',
      visible = true,
      selected = false,
      color = '#000000',
      minValue = 0.0,
      maxValue = 100.0
    } = params;

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
