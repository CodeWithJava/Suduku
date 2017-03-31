package suduku;

public class DLXNode {
	/*
	** Exact Cover Problem
	** Quote from http://www.cnblogs.com/grenet/p/3145800.html
	** Author: 万仓一黍
	**
	**
	*/
	
		int r;
		int c;
		DLXNode U;
		DLXNode D;
		DLXNode L;
		DLXNode R;

		DLXNode()
		{
			r = 0;
			c = 0;
		}

		DLXNode(int r, int c)
		{
			this.r = r;
			this.c = c;
		}

		DLXNode(int r, int c, DLXNode U, DLXNode D, DLXNode L, DLXNode R)
		{
			this.r = r;
			this.c = c;
			this.U = U;
			this.D = D;
			this.L = L;
			this.R = R;
		}

		public void deleteLR()
		{
			L.R = R;
			R.L = L;
		}

		public void deleteUD()
		{
			U.D = D;
			D.U = U;
		}

		public void resumeLR()
		{
			L.R = this;
			R.L = this;
		}

		public void resumeUD()
		{
			U.D = this;
			D.U = this;
		}
}

