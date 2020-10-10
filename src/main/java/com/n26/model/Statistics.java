package com.n26.model;

import java.util.Objects;

/***
 * @author Yuliia Brendel
 */
public class Statistics {

	private volatile String sum; 
	private volatile String avg; 
	private volatile String max; 
	private volatile String min; 
	private volatile long count;
	
	public Statistics() {
		this.sum = "0.00";
		this.avg = "0.00";
		this.max = "0.00";
		this.min = "0.00";
		this.count = 0;
	}

	public Statistics(String sum, String avg, String max, String min, long count) {
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public String getSum() {
		return sum;
	}
	
	public String getAvg() {
		return avg;
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Statistics)) return false;
        Statistics that = (Statistics) o;
        return count == that.count &&
                Objects.equals(sum, that.sum) &&
                Objects.equals(avg, that.avg) &&
                Objects.equals(max, that.max) &&
                Objects.equals(min, that.min);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, avg, max, min, count);
    }
}
