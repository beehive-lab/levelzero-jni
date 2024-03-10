__kernel void copyTest(__global uchar *_heap_base)
{
	int i_8, i_7, i_1, i_2;
	ulong ul_0, ul_6;
	long l_3, l_5, l_4;

	__global ulong *_frame = (__global ulong *) &_heap_base[0];

	ul_0  =  (ulong) _frame[3];
	i_1  =  get_global_id(0);
	i_2  =  i_1;
	l_3  =  (long) i_2;
	l_4  =  l_3 << 3;  // Long buffer
	l_5  =  l_4 + 16L; // Randomly starting in position 16
	ul_6  =  ul_0 + l_5;
	*((__global int *) ul_6)  =  555;
}
