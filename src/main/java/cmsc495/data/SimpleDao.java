package cmsc495.data;

public interface SimpleDao<T> {
  public void create(T item);
  public T read(int index);
  public void update(T item);
  public void delete(int index);
}
