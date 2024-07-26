import { withBasePath } from "@/lib";

interface Props extends Record<string, any> {
  basePath?: boolean;
  src: string;
}
export default (props: Props) => {
  const { basePath, src } = props;
  const needWithBasePath = basePath == false ? basePath : true;
  return needWithBasePath ? (
    <img {...props} src={withBasePath(src)} />
  ) : (
    <img {...props} />
  );
};
