import { useEffect } from "react";

interface Props {
  ticker: string;
}

const TradingViewChart = ({ ticker }: Props) => {
  const containerId = "tradingview_container";

  useEffect(() => {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = "";

    const script = document.createElement("script");
    script.src = "https://s3.tradingview.com/tv.js";
    script.async = true;

    script.onload = () => {
      // @ts-ignore
      new window.TradingView.widget({
        autosize: true,
        symbol: `NASDAQ:${ticker}`,
        interval: "D",
        theme: "light",
        style: "1",
        locale: "en",
        container_id: containerId,

        // studies: [
        //   // 이동평균
        //   {
        //     id: "MASimple@tv-basicstudies",
        //     inputs: { length: 5 },
        //     styles: { plot: { color: "#ff0000", linewidth: 2 } },
        //   },
        //   {
        //     id: "MASimple@tv-basicstudies",
        //     inputs: { length: 20 },
        //     styles: { plot: { color: "#00aa00", linewidth: 2 } },
        //   },
        //   {
        //     id: "MASimple@tv-basicstudies",
        //     inputs: { length: 60 },
        //     styles: { plot: { color: "#0066ff", linewidth: 2 } },
        //   },
        //   {
        //     id: "MASimple@tv-basicstudies",
        //     inputs: { length: 120 },
        //     styles: { plot: { color: "#800080", linewidth: 2 } },
        //   },
        //
        //   // 일목균형표
        //   {
        //     id: "IchimokuCloud@tv-basicstudies",
        //   },
        // ],
      });
    };

    document.body.appendChild(script);

    return () => {
      container.innerHTML = "";
    };
  }, [ticker]);

  return <div id={containerId} style={{ height: "500px" }} />;
};

export default TradingViewChart;
