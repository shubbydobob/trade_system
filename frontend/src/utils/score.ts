export const getScoreColor = (score: number) => {
    if (score >= 80) return "#e53935";   // 강한 매수
    if (score >= 65) return "#fb8c00";   // 관심
    if (score >= 50) return "#fdd835";   // 중립
    return "#9e9e9e";                    // 제외
};
