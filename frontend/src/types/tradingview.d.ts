export {};

declare global {
    interface Window {
        TradingView: {
            widget: (config: any) => any;
        };
    }
}
