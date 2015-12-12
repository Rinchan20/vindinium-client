package com.brianstempin.vindiniumclient.bot.simple;

public class Node<T> {
    private int id;
    private double distance;
    private T data;
    private T tileStatus;

    public Node(int id) {
        this.id = id;
        this.data = null;
        this.tileStatus = null;
        this.distance = -1;
    }
    public Node(int id, T data) {
        this.id = id;
        this.data = data;
        this.tileStatus = null;
        this.distance = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
	 * @return the tileStatus
	 */
	public T getTileStatus() {
		return tileStatus;
	}
	/**
	 * @param tileStatus the tileStatus to set
	 */
	public void setTileStatus(T tileStatus) {
		this.tileStatus = tileStatus;
	}
	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
    public String toString() {
        return "Node{" +
            "id=" + id +
            ", data=" + data +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node<?> node = (Node<?>) o;

        if (getId() != node.getId()) return false;
        return !(getData() != null ? !getData().equals(node.getData()) : node.getData() != null);

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }
}
