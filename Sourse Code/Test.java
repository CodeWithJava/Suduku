import java.util.Random;
public class Test
{
	public static void main(String [] args)
	{
		Random r = new Random();
		int n = 11;
		char [][] matrix = new char [9][9];

		for(int i = 0;i < 9;i++)
			for(int j = 0;j < 9;j++)
				matrix[i][j] = '.';

		while(n > 0)
		{
			int x = r.nextInt(9) + 1;
			int y = r.nextInt(9) + 1;

			if(matrix[x][y] != '.')
				continue;

			
		}
	}
}