export const formatNumber = (value?: number) => {
    if (value === null || value === undefined) return "-";
    return value.toLocaleString();
};
