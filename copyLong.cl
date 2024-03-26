__kernel void copyData(__global long* input, __global long* output) {
	uint idx = get_global_id(0);
	output[idx] = input[idx];
}
