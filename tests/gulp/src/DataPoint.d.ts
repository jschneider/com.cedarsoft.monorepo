export declare type ValueWithId = {
    readonly processValueId: string;
    readonly value: number;
};
export declare type DataPoint = {
    readonly values: Array<ValueWithId>;
    readonly timestamp: Date;
};
