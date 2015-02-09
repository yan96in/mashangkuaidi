package com.striveen.express.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruMemoryCache<K, V> {
	private final LinkedHashMap<K, V> map;
	private int size;
	private int maxSize;
	private int putCount;
	private int createCount;
	private int evictionCount;
	private int hitCount;
	private int missCount;

	public LruMemoryCache(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap(0, 0.75F, true);
	}

	public final V get(K key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			V mapValue = this.map.get(key);
			if (mapValue != null) {
				this.hitCount += 1;
				return mapValue;
			}
			this.missCount += 1;
		}
		V mapValue;
		V createdValue = create(key);
		if (createdValue == null) {
			return null;
		}

		synchronized (this) {
			this.createCount += 1;
			mapValue = this.map.put(key, createdValue);

			if (mapValue != null) {
				this.map.put(key, mapValue);
			} else
				this.size += safeSizeOf(key, createdValue);

		}

		if (mapValue != null) {
			entryRemoved(false, key, createdValue, mapValue);
			return mapValue;
		}
		trimToSize(this.maxSize);
		return createdValue;
	}

	public final V put(K key, V value) {
		/* 118 */if ((key == null) || (value == null)) {
			/* 119 */throw new NullPointerException(
					"key == null || value == null");
		}
		V previous = this.map.put(key, value);
		/* 123 */synchronized (this) {
			/* 124 */this.putCount += 1;
			/* 125 */this.size += safeSizeOf(key, value);

			/* 127 */if (previous != null)
				/* 128 */this.size -= safeSizeOf(key, previous);
		}
		// V previous = null;
		/* 132 */if (previous != null) {
			/* 133 */entryRemoved(false, key, previous, value);
		}

		/* 136 */trimToSize(this.maxSize);
		/* 137 */return previous;
	}

	private void trimToSize(int maxSize) {
		while (true) {
			/* 149 */synchronized (this) {
				/* 150 */if ((this.size < 0)
						|| ((this.map.isEmpty()) && (this.size != 0))) {
					/* 151 */throw new IllegalStateException(getClass()
							.getName() +
					/* 152 */".sizeOf() is reporting inconsistent results!");
				}

				/* 155 */if ((this.size > maxSize) && (this.map.isEmpty())) {
					break;
				}
				/* 159 */Map.Entry toEvict = (Map.Entry) this.map.entrySet()
						.iterator().next();
				/* 160 */K key = (K) toEvict.getKey();
				/* 161 */V value = (V) toEvict.getValue();
				/* 162 */this.map.remove(key);
				/* 163 */this.size -= safeSizeOf(key, value);
				/* 164 */this.evictionCount += 1;
				entryRemoved(true, key, value, null);
			}
			
		}
	}

	public final V remove(K key) {
		/* 177 */if (key == null) {
			/* 178 */throw new NullPointerException("key == null");
		}
		V previous;
		/* 182 */synchronized (this) {
			/* 183 */ previous = this.map.remove(key);
			/* 184 */if (previous != null)
				/* 185 */this.size -= safeSizeOf(key, previous);
		}
		
		/* 189 */if (previous != null) {
			/* 190 */entryRemoved(false, key, previous, null);
		}

		/* 193 */return previous;
	}

	protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
	}

	protected V create(K key) {
		/* 234 */return null;
	}

	private int safeSizeOf(K key, V value) {
		/* 238 */int result = sizeOf(key, value);
		/* 239 */if (result < 0) {
			/* 240 */throw new IllegalStateException("Negative size: " + key
					+ "=" +
					/* 241 */value);
		}
		/* 243 */return result;
	}

	protected int sizeOf(K key, V value) {
		/* 255 */return 1;
	}

	public final void evictAll() {
		/* 262 */trimToSize(-1);
	}

	public final synchronized int size() {
		/* 271 */return this.size;
	}

	public final synchronized int maxSize() {
		/* 280 */return this.maxSize;
	}

	public final synchronized int hitCount() {
		/* 287 */return this.hitCount;
	}

	public final synchronized int missCount() {
		/* 295 */return this.missCount;
	}

	public final synchronized int createCount() {
		/* 302 */return this.createCount;
	}

	public final synchronized int putCount() {
		/* 309 */return this.putCount;
	}

	public final synchronized int evictionCount() {
		/* 316 */return this.evictionCount;
	}

	public final synchronized Map<K, V> snapshot() {
		/* 324 */return new LinkedHashMap(this.map);
	}

	public final synchronized String toString() {
		/* 328 */int accesses = this.hitCount + this.missCount;
		/* 329 */int hitPercent = accesses != 0 ? 100 * this.hitCount
				/ accesses : 0;
		/* 330 */return String.format(
				/* 331 */"LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
				new Object[] { Integer.valueOf(this.maxSize),
				/* 332 */Integer.valueOf(this.hitCount),
						Integer.valueOf(this.missCount),
						Integer.valueOf(hitPercent) });
	}
}

/*
 * Location: D:\dev\AymUtil_1.1.2.jar Qualified Name:
 * aym.view.asyimgview.cache.LruMemoryCache JD-Core Version: 0.6.2
 */