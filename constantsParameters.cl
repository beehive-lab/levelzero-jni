__kernel void initArray(__global int* data, const int value) {
	uint idx = get_global_id(0);
	data[idx] = value * 100;
}
