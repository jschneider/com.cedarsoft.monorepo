export type ValueWithId = {
  readonly processValueId: string;
  readonly value: number;
}

export type DataPoint = {
  readonly values: Array<ValueWithId>;
  readonly timestamp: Date;
}