/*
** Exact Cover Problem
** Quote from http://www.cnblogs.com/grenet/p/3145800.html
** Author: 万仓一黍
**
**
*/
import java.util.List;
import java.util.ArrayList;

public class DLX
{
	private static final int ROW = 4096 + 50;
	private static final int COL = 1024 + 50;
	private static final int N = 4 * 9 * 9;
	private static final int M = 3;
	private static final int N = 9;

	DLXNode [] row = new DLXNode[ROW];
	DLXNode [] col = new DLXNode[COL];
	DLNode head;

	private int n;
	private int limitation = 2;
	private int [] size = new int [COL];
	int [][] data = new int [N][N];

	List<int [][]> solutions;

	public DLX(int r, int c)
	{
		n = M * M;
		head = new DLXNode(-1, -1);
		head.U = head;
		head.D = head;
		head.L = head;
		head.R = head;

		for(int i = 0;i < c;i++)
		{
			col[i] = new DLXNode(r, i);
			col[i].L = head;
			col[i].R = head.R;
			col[i].L.R = col[i];
			col[i].R.L = col[i];
			col[i].U = col[i];
			col[i].D = col[i];
			size[i] = 0;
		}

		for(int i = r - 1;i >= 0;i--)
		{
			row[i] = new DLXNode(i, c);
			row[i].U = head;
			row[i].D = head.D;
			row[i].U.D = row[i];
			row[i].D.U = row[i];
			row[i].L = row[i];
			row[i].R = row[i];
		}
	}


	private void addNode(int i, int j, int k)
	{
		int r = (i * n + j) * n + k;
		addNode(r, i * n + k - 1);
		addNode(r, n * n + j * n + k - 1);
		addNode(r, 2 * n * n + block(i, j) * n + k - 1);
		addNode(r, 3 * n * n + i * n + j);
	}

	public void addNode(int r, int c)
	{
		DLXNode p = new DLXNode(r, c);
		p.R = row[r];
		P.L = row[r].L;
		P.L.R = p;
		p.R.L = p;

		p.U = col[c];
		p.D = col[c].D;
		p.U.D = p;
		p.D.U = p;
		size[c]++;
	}

	private int block(int x, int y)
	{
		return x / M * M + y / M;
	}

	public void cover(int c)
	{
		if(c == N)
			return;

		col[c].deleteLR();

		for(DLXNode C = col[c].D;C != col[c];C = C.D)
		{
			if(C.c == N)
				continue;

			for(DLXNode R = C.L;R != C;R = R.L)
			{
				if(R.c == N)
					continue;

				size[R.c]--;
				R.deleteUD();
			}

			C.deleteLR();
		}
	}

	public void resume(int c)
	{
		if(c == N)
			return;

		for(DLXNode C = col[c].U;C != col[c];C = C.U)
		{
			if(C.c == N)
				continue;

			C.resumeLR();

			for(DLXNode R = C.R;R != C;R = R.R)
			{
				if(R.c == N)
					continue;

				size[R.c]++;
				R.resumeUD();
			}
		}

		col[c].resumeLR();
	}

	
}