import type {ReactNode} from "react";

interface Props {
  title: string;
  children: ReactNode;
}

const PageLayout = ({ title, children }: Props) => {
  return (
    <div style={{ padding: "24px" }}>
      <h2>{title}</h2>
      {children}
    </div>
  );
};

export default PageLayout;
