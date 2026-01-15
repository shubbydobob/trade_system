import {useEffect, useState} from "react";
import axios from "axios";
import StockAnalyzeTable from "./StockAnalyzeTable.tsx";

export default function StockAnalyzeSection({limit = 20}: {limit?: number}) {
    const [data, setData] = useState<any[]>([]);

    useEffect(() => {
        axios
            .get(`/api/stocks/analyze?limit=${limit}`)
            .then((res) => {
                const list = res.data?.data ?? [];
                setData(Array.isArray(list) ? list : []);
            })
            .catch(() => setData([]));
    }, [limit]);

    return (
        <div>
            <h2>📈 전략 점수 상위 종목</h2>
            <StockAnalyzeTable data={data} />
        </div>
    );
}
