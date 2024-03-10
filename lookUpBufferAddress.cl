__kernel void lookUp(__global long *heap, __global long* output) {
	output[get_global_id(0)]  =  (ulong) heap;
}
