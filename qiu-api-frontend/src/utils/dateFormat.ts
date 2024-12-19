import dayjs from 'dayjs'

export const dateFormat = (date: string | undefined) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}
